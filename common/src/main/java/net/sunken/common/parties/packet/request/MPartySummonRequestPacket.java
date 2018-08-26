package net.sunken.common.parties.packet.request;

import lombok.Getter;

import java.util.UUID;

/** Sent when a player wants to summon party members to their server */
public class MPartySummonRequestPacket extends MPartyRequestPacket {

    @Getter
    private final String serverName;

    public MPartySummonRequestPacket(UUID requesting, String serverName) {
        super(requesting);
        this.serverName = serverName;
    }
}
