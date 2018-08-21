package net.sunken.common.friend.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

public class FriendRequestPacket extends Packet {

    @Getter
    private UUID creator;
    @Getter
    private String toInvite;

    public FriendRequestPacket (UUID creator, String toInvite) {
        this.creator = creator;
        this.toInvite = toInvite;
    }

}
