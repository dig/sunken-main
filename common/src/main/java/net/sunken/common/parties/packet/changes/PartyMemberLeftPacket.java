package net.sunken.common.parties.packet.changes;

import lombok.Getter;
import net.sunken.common.packet.Packet;
import net.sunken.common.parties.data.Party;

import java.util.UUID;

/** Packet sent when a member (NOT LEADER) leaves a party. */
public class PartyMemberLeftPacket extends Packet {

    @Getter
    private final UUID memberLeftUUID;
    @Getter
    private final String nameLeft;
    @Getter
    private final Party party;

    public PartyMemberLeftPacket(UUID memberLeftUUID, String nameLeft, Party party) {
        this.memberLeftUUID = memberLeftUUID;
        this.nameLeft = nameLeft;
        this.party = party;
    }
}
