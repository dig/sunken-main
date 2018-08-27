package net.sunken.common.friend.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

public class MFriendAcceptPacket extends Packet {

    @Getter
    private UUID creator;
    @Getter
    private String target;

    @Getter
    private boolean deny;

    public MFriendAcceptPacket (UUID creator, String target, boolean deny) {
        this.creator = creator;
        this.target = target;
        this.deny = deny;
    }

}