package net.sunken.common.parties.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;
import net.sunken.common.parties.service.status.PartyCreateStatus;
import net.sunken.common.util.PlayerDetail;

public class PartyCreatePacket extends Packet {

    @Getter
    private final PlayerDetail creator;
    @Getter
    private final PlayerDetail invitee;
    @Getter
    private final PartyCreateStatus createStatus;

    public PartyCreatePacket(PlayerDetail creator, PlayerDetail invitee, PartyCreateStatus createStatus) {
        this.creator = creator;
        this.invitee = invitee;
        this.createStatus = createStatus;
    }
}
