package net.sunken.common.parties.packet.changes;

import lombok.Getter;
import net.sunken.common.packet.Packet;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.status.PartyCreateStatus;

/** Sent by Master when a party has been created */
public class PartyCreatedPacket extends Packet {

    @Getter
    private final PartyPlayer creator;
    @Getter
    private final PartyPlayer invitee;
    @Getter
    private final PartyCreateStatus createStatus;

    public PartyCreatedPacket(PartyPlayer creator, PartyPlayer invitee, PartyCreateStatus createStatus) {
        this.creator = creator;
        this.invitee = invitee;
        this.createStatus = createStatus;
    }
}
