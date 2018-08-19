package net.sunken.common.player.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

public class PlayerQuitPacket extends Packet {

    @Getter
    private String name;
    @Getter
    private UUID uuid;

    public PlayerQuitPacket (String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

}
