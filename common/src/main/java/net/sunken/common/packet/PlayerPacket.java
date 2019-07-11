package net.sunken.common.packet;

import lombok.Getter;

import java.util.UUID;

public abstract class PlayerPacket extends Packet {

    @Getter
    private final UUID playerTargeted;

    public PlayerPacket(UUID playerTargeted) {
        this.playerTargeted = playerTargeted;
    }
}
