package net.sunken.common.parties.service;

import net.sunken.common.Common;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parkour.ParkourRedisHelper;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.PartyDisbandedPacket;
import net.sunken.common.parties.packet.PartyInviteSendPacket;
import net.sunken.common.parties.packet.PartyMemberLeftPacket;
import net.sunken.common.parties.service.status.PartyCreateStatus;
import net.sunken.common.player.PlayerRank;
import redis.clients.jedis.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.Level;

import static com.google.common.base.Preconditions.checkState;

public class RedisPartyService implements PartyService {

    public RedisPartyService() {}

    @Override
    public Party getPartyByUUID(UUID uuid) {
        Party party = null;

        JedisPool pool = Common.getInstance().getRedis().getJedisPool();
        Jedis jedis = pool.getResource();

        try {
            String partyKey = "party:" + uuid.toString();

            String leaderUniqueIdStr = jedis.get(partyKey + ":leader");
            String createdAt = jedis.get(partyKey + ":created_at");

            ScanParams params = new ScanParams();
            params.count(20);
            params.match(partyKey + ":members:*");

            ScanResult<String> memberScan = jedis.scan("0", params);
            List<String> result = memberScan.getResult();
            Set<PartyPlayer> allMembers = new HashSet<>();

            for (String memberKey : result) {
                Map<String, String> memberInformation = jedis.hgetAll(memberKey);
                PartyPlayer member = new PartyPlayer();
                memberInformation.forEach((field, value) -> {
                    switch (field) {
                        case "uuid":
                            member.setUniqueId(UUID.fromString(value));
                            break;
                        case "name":
                            member.setName(value);
                            break;
                        case "player_rank":
                            member.setRank(PlayerRank.valueOf(value));
                            break;
                        default:
                            assert false : "unknown field";
                    }
                });
                allMembers.add(member);
            }

           party = new Party(uuid,
                             UUID.fromString(leaderUniqueIdStr),
                             allMembers,
                             Long.parseLong(createdAt));
        } catch (Exception e) {
            e.printStackTrace();
            pool.returnBrokenResource(jedis);
        } finally {
            pool.returnResource(jedis);
        }

        return party;
    }

    @Override
    public PartyCreateStatus createParty(PartyPlayer leader, PartyPlayer toInvite) {
        JedisPool pool = Common.getInstance().getRedis().getJedisPool();
        Jedis jedis = pool.getResource();

        try {
            // check whether either player is in a party already, if so, deny the creation

            boolean partyWithLeaderInExists = jedis.exists("party:*:members:" + leader.getUniqueId());
            if (partyWithLeaderInExists) {
                return PartyCreateStatus.INVITER_ALREADY_IN_PARTY;
            }
            boolean partyWithInviteeInExists = jedis.exists("party:*:members:" + leader.getUniqueId());
            if (partyWithInviteeInExists) {
                return PartyCreateStatus.INVITEE_ALREADY_IN_PARTY;
            }

            // no party exists with either the inviter or invitee in it, go ahead and create it

            String partyUUID = UUID.randomUUID().toString();
            Transaction transaction = jedis.multi(); // start transaction

            // leader information
            transaction.set("party:" + partyUUID + ":leader:" + leader.getUniqueId().toString(), "");
            transaction.set("party:" + partyUUID + ":leader", leader.getUniqueId().toString());
            // created at
            long now = System.currentTimeMillis();
            transaction.set("party:" + partyUUID + ":created_at", String.valueOf(now));
            // all members including leader themselves
            transaction.hmset("party:" + partyUUID + ":members:" + leader.getUniqueId(), leader.toMap());
            transaction.hmset("party:" + partyUUID + ":members:" + toInvite.getUniqueId(), toInvite.toMap());

            transaction.exec(); // execute transaction

            PartyInviteSendPacket partyInviteSendPacket = new PartyInviteSendPacket();
            PacketUtil.sendPacket(partyInviteSendPacket);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
        } finally {
            pool.returnResource(jedis);
        }
        // successful creation
        return PartyCreateStatus.SUCCESS;
    }

    @Override
    public void leaveParty(UUID leaving) {
        JedisPool pool = Common.getInstance().getRedis().getJedisPool();
        Jedis jedis = pool.getResource();

        try {
            // was the party deleted as the player being a leader?
            // party UUID retrieved if the player leaving was a leader of a party
            final UUID uuidAsLeader = this.getPartyUUIDFromQuery(jedis, "party:*:leader:" + leaving.toString());
            if (uuidAsLeader != null) {
                Party party = this.getPartyByUUID(uuidAsLeader);

                String partyKey = "party:" + party.getPartyUUID().toString();
                jedis.del(partyKey + ":leader");
                jedis.del(partyKey + ":created_at");

                for(PartyPlayer ply : party.getAllMembers()){
                    jedis.del(partyKey + ":members:" + ply.getUniqueId().toString());
                }

                PartyDisbandedPacket partyDisbandedPacket = new PartyDisbandedPacket(party);
                PacketUtil.sendPacket(partyDisbandedPacket);
            } else {
                // player is not a party leader, may be a member?
                final UUID uuidAsMember = this.getPartyUUIDFromQuery(jedis, "party:*:members:" + leaving.toString());
                if (uuidAsMember != null) {
                    Party party = this.getPartyByUUID(uuidAsMember);
                    // delete the member from the party
                    long amountDeleted = jedis
                            .del("party:" + party.getPartyUUID().toString() + ":members:" + leaving.toString());
                    if (amountDeleted > 0) {
                        PartyMemberLeftPacket partyMemberLeftPacket = new PartyMemberLeftPacket(leaving, party);
                        PacketUtil.sendPacket(partyMemberLeftPacket);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            pool.returnBrokenResource(jedis);
        } finally {
            pool.returnResource(jedis);
        }
    }

    /** Return the party UUID found from a wildcard query (e.g. scan query etc.) */
    @Nullable
    private UUID getPartyUUIDFromQuery(Jedis jedis, String query) {
        ScanParams params = new ScanParams();
        params.count(20);
        params.match(query);

        ScanResult<String> scan = jedis.scan("0", params);
        List<String> result = scan.getResult();

        if (result.size() > 0) {
            String first = result.get(0);
            String[] split = first.split(":");
            if (1 < split.length) {
                String partyUUID = split[1];
                if (partyUUID != null) {
                    return UUID.fromString(partyUUID);
                }
            }
        }
        return null;
    }
}
