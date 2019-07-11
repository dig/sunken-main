package net.sunken.common.friend.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

public class MFriendRequestPacket extends Packet {

    @Getter
    private UUID creator;
    @Getter
    private String toInvite;

    public MFriendRequestPacket (UUID creator, String toInvite) {
        this.creator = creator;
        this.toInvite = toInvite;
    }

}