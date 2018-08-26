package net.sunken.master.party;

import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.packet.MustBeInPartyPacket;

import java.util.UUID;

// WIP
public final class PartyPreconditions {

    public static Party mustBeInParty(UUID uuid) {
        Party party = PartyManager.getPartyByPlayer(uuid);
        if (party == null) {
            PacketUtil.sendPacket(new MustBeInPartyPacket(uuid));
            return null;
        }
        return null;
    }

    private PartyPreconditions() {
    }
}
