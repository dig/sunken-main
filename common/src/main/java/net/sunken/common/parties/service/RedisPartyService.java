package net.sunken.common.parties.service;

import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.service.status.PartyCreateStatus;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class RedisPartyService implements PartyService {

    private final JedisPool pool;

    public RedisPartyService(JedisPool pool) {
        this.pool = pool;
    }

    @Override
    public PartyCreateStatus createParty(PartyPlayer leader, PartyPlayer toInvite) {
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

            // party identifier
            String partyUUID = UUID.randomUUID().toString();
            // leader information
            jedis.hmset("party:" + partyUUID + ":leader:" + leader.getUniqueId(), leader.toMap());
            // created at
            long now = System.currentTimeMillis();
            jedis.set("party:" + partyUUID + ":created_at", String.valueOf(now));
            // all members including leader themselves
            jedis.hmset("party:" + partyUUID + ":members:" + leader.getUniqueId(), leader.toMap());
            jedis.hmset("party:" + partyUUID + ":members:" + toInvite.getUniqueId(), toInvite.toMap());

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
        Jedis jedis = pool.getResource();
        try {
            // was the party deleted as the player being a leader?
            boolean deletedAsLeader = this.deletePartyFromQuery(jedis, "party:*:leader:" + leaving.toString());

            // player is not a party leader, may be a member?
            if (!deletedAsLeader) {
                this.deletePartyFromQuery(jedis, "party:*:members:" + leaving.toString());
            }
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
        } finally {
            pool.returnResource(jedis);
        }
    }

    /** Return whether the party was found from the query (e.g. scan query etc.) and deleted successfully */
    private boolean deletePartyFromQuery(Jedis jedis, String query) {
        ScanResult<String> scan = jedis.scan(query);
        List<String> result = scan.getResult();
        if (result.size() > 0) {
            String first = result.get(0);
            String partyKey = this.getPartyKeyFromQuery(first);
            if (partyKey != null) {
                jedis.del(first);
                return true;
            }
        }
        return false;
    }

    /** Returns party:partyUUID from a string such as party:partyUUID:leader:leaderUUID */
    @Nullable
    private String getPartyKeyFromQuery(String query) {
        String[] split = query.split(":");
        if (split[0] != null && split[1] != null) {
            return split[0] + ":" + split[1];
        }
        return null;
    }
}
