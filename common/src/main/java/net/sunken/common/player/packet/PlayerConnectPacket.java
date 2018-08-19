package net.sunken.common.player.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

public class PlayerConnectPacket extends Packet {

    @Getter
    private UUID uuid;

    @Getter
    private String serverName;

    public PlayerConnectPacket (UUID uuid, String serverName){
        this.uuid = uuid;
        this.serverName = serverName;
    }
}
