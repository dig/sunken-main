package net.sunken.common.parties;

import com.google.common.collect.ImmutableSet;
import net.sunken.common.packet.PacketUtil;
import redis.clients.johm.JOhm;

import java.util.List;

public class RedisPartyService implements PartyService {

    @Override
    public PartyCreateStatus createParty(PartyPlayer leader, PartyPlayer toInvite) {
        List<Object> bySameCreator = JOhm.find(Party.class, Party.LEADER_KEY, leader.getUuid());
        if (bySameCreator.size() > 0) {
            return PartyCreateStatus.ALREADY_IN_PARTY;
        }

        Party newParty = new Party(leader, ImmutableSet.of(toInvite), System.currentTimeMillis());
        JOhm.save(newParty);

        PartyInviteSendPacket partyInviteSendPacket = new PartyInviteSendPacket(leader.getUuid(), toInvite.getUuid());
        PacketUtil.sendPacket(partyInviteSendPacket);

        return PartyCreateStatus.SUCCESS;
    }
}
