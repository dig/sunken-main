package net.sunken.common.parties.packet;

import lombok.Getter;
import net.sunken.common.packet.PlayerPacket;

import java.util.UUID;

public class MustBeLeaderPacket extends PlayerPacket {

    @Getter
    private String message = "You must be the leader!";

    public MustBeLeaderPacket(UUID playerTargeted) {
        super(playerTargeted);
    }

    public MustBeLeaderPacket(UUID playerTargeted, String message) {
        super(playerTargeted);
        this.message = message;
    }
}
