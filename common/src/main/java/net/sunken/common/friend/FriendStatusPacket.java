package net.sunken.common.friend;

import lombok.Getter;
import net.sunken.common.packet.Packet;
import net.sunken.common.util.PlayerDetail;

public class FriendStatusPacket extends Packet {

    @Getter
    private PlayerDetail creator;
    @Getter
    private String toInvite;

    @Getter
    private FriendStatus status;

    public FriendStatusPacket (PlayerDetail creator, String toInvite, FriendStatus status) {
        this.creator = creator;
        this.toInvite = toInvite;
        this.status = status;
    }

}
