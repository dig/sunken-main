package net.sunken.common.parties.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;

/** Packet sent when a member joins a party */
public class PartyMemberJoinedPacket extends Packet {

    @Getter
    private final PartyPlayer memberJoined;
    @Getter
    private final Party party;

    public PartyMemberJoinedPacket(PartyPlayer memberJoined, Party party) {
        this.memberJoined = memberJoined;
        this.party = party;
    }
}
