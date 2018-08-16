package net.sunken.common.parties;

import com.google.common.collect.ImmutableSet;
import net.sunken.common.packet.PacketUtil;
import redis.clients.johm.JOhm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class RedisPartyService implements PartyService {

    @Override
    public PartyCreateStatus createParty(PartyPlayer leader, PartyPlayer toInvite) {
        List<Party> bySameCreator = JOhm.find(Party.class, Party.LEADER_KEY, leader.getUUID().toString());
        if (bySameCreator.size() > 0) {
            return PartyCreateStatus.ALREADY_IN_PARTY;
        }

        Party newParty = new Party(leader.getUUID(), new HashSet<>(Arrays.asList(leader, toInvite)), System.currentTimeMillis());
        JOhm.save(newParty);

        PartyInviteSendPacket partyInviteSendPacket = new PartyInviteSendPacket(leader.getUUID(), toInvite.getUUID());
        PacketUtil.sendPacket(partyInviteSendPacket);

        return PartyCreateStatus.SUCCESS;
    }

    @Override
    public void leaveParty(UUID leaving) {
//        JOhm.find(Party.class, Party.ALL_MEMBERS_KEY, )
//        Jedis j = 2;
//        j.exists("Party:allMembers:PartyPlayer")
    }
}
