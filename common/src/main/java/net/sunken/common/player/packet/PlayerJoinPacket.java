package net.sunken.common.player.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;
import org.bson.Document;

import java.util.UUID;

public class PlayerJoinPacket extends Packet {

    @Getter
    private String name;
    @Getter
    private UUID uuid;

    @Getter
    private Document document;

    public PlayerJoinPacket (String name, UUID uuid, Document document) {
        this.name = name;
        this.uuid = uuid;
        this.document = document;
    }

}
