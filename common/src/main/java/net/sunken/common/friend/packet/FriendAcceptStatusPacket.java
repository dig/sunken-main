package net.sunken.common.friend.packet;

import lombok.Getter;
import net.sunken.common.friend.data.FriendStatus;
import net.sunken.common.packet.Packet;
import net.sunken.common.util.PlayerDetail;
import org.bson.types.ObjectId;

public class FriendAcceptStatusPacket extends Packet {

    @Getter
    private PlayerDetail creator;
    @Getter
    private String target;

    @Getter
    private FriendStatus status;

    public FriendAcceptStatusPacket (PlayerDetail creator, String target, FriendStatus status) {
        this.creator = creator;
        this.target = target;
        this.status = status;
    }
}
