package net.sunken.common.friend.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

public class MFriendRemovePacket extends Packet {

    @Getter
    private UUID creator;
    @Getter
    private String target;

    public MFriendRemovePacket (UUID creator, String target) {
        this.creator = creator;
        this.target = target;
    }

}
