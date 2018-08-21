package net.sunken.common.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

public abstract class PlayerPacket extends Packet {

    @Getter
    private final UUID playerTargeted;

    protected PlayerPacket(UUID playerTargeted) {
        this.playerTargeted = playerTargeted;
    }
}
