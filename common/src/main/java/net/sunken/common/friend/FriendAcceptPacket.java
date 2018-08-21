package net.sunken.common.friend;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

public class FriendAcceptPacket extends Packet {

    @Getter
    private UUID creator;
    @Getter
    private String target;

    public FriendAcceptPacket (UUID creator, String target) {
        this.creator = creator;
        this.target = target;
    }

}
