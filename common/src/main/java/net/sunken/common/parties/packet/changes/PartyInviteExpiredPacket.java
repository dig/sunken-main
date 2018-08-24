package net.sunken.common.parties.packet.changes;

import lombok.Getter;
import net.sunken.common.packet.Packet;
import net.sunken.common.util.PlayerDetail;

public class PartyInviteExpiredPacket extends Packet {

    @Getter
    private final PlayerDetail inviter;
    @Getter
    private final PlayerDetail invitee;

    public PartyInviteExpiredPacket(PlayerDetail inviter, PlayerDetail invitee) {
        this.inviter = inviter;
        this.invitee = invitee;
    }
}
