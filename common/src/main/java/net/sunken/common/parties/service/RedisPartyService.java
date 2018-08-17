package net.sunken.common.parties.service;

import com.google.gson.Gson;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.PartyDisbandedPacket;
import net.sunken.common.parties.packet.PartyInviteSendPacket;
import net.sunken.common.parties.service.status.PartyCreateStatus;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

public class RedisPartyService implements PartyService {

    private static final Gson GSON = new Gson();

    private final Jedis jedis;

    public RedisPartyService(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public PartyCreateStatus createParty(PartyPlayer leader, PartyPlayer toInvite) {
        boolean partyWithLeaderExists = this.jedis.exists(Party.PARTY_KEY + leader.getUniqueId().toString());
        if (partyWithLeaderExists) {
            return PartyCreateStatus.ALREADY_IN_PARTY;
        }

        Party newParty = new Party(leader.getUniqueId(), new HashSet<>(Arrays.asList(leader, toInvite)),
                System.currentTimeMillis());
        String partyAsJson = GSON.toJson(newParty);
        // index by leader's unique ID
        jedis.set(Party.PARTY_KEY + leader.getUniqueId().toString(), partyAsJson);

        // send the party invite send packet
        PartyInviteSendPacket partyInviteSendPacket = new PartyInviteSendPacket(leader.getUniqueId(),
                toInvite.getUniqueId());
        PacketUtil.sendPacket(partyInviteSendPacket);

        return PartyCreateStatus.SUCCESS;
    }

    @Override
    public void leaveParty(UUID leaving) {
        // the player leaving the party is a party leader
        boolean partyWithLeaderExists = this.jedis.exists(Party.PARTY_KEY + leaving.toString());
        if (partyWithLeaderExists) {
            jedis.del(Party.PARTY_KEY + leaving.toString());
            PartyDisbandedPacket partyDisbandedPacket = new PartyDisbandedPacket();
            PacketUtil.sendPacket(partyDisbandedPacket);
            return;
        }

        
    }
}
