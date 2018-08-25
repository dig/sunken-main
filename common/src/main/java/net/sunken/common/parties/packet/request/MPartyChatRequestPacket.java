package net.sunken.common.parties.packet.request;

import lombok.Getter;

import java.util.UUID;

/** Sent when a player wants to chat amongst their party members in the party channel */
public class MPartyChatRequestPacket extends MPartyRequestPacket {

    @Getter
    private String message;

    public MPartyChatRequestPacket(UUID requesting, String message) {
        super(requesting);
        this.message = message;
    }
}
