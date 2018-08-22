package net.sunken.common.parties.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.service.status.PartyCreateStatus;

public class PartyCreatePacket extends Packet {

    @Getter
    private final PartyPlayer creator;
    @Getter
    private final PartyPlayer invitee;
    @Getter
    private final PartyCreateStatus createStatus;

    public PartyCreatePacket(PartyPlayer creator, PartyPlayer invitee, PartyCreateStatus createStatus) {
        this.creator = creator;
        this.invitee = invitee;
        this.createStatus = createStatus;
    }
}
