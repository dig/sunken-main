package net.sunken.common.friend;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

public class FriendStatusPacket extends Packet {

    @Getter
    private UUID creator;
    @Getter
    private String toInvite;

    @Getter
    private FriendStatus status;

    public FriendStatusPacket (UUID creator, String toInvite, FriendStatus status) {
        this.creator = creator;
        this.toInvite = toInvite;
        this.status = status;
    }

}
