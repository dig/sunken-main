package net.sunken.common.parties.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;
import net.sunken.common.parties.data.Party;

/**
 * Packet sent when a leader leaves the party causing
 * the party to disband.
 */
public class PartyDisbandedPacket extends Packet {

    @Getter
    private final Party partyDeleted;

    public PartyDisbandedPacket(Party partyDeleted) {
        this.partyDeleted = partyDeleted;
    }
}
