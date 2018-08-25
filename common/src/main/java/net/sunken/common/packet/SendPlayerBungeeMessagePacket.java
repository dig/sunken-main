package net.sunken.common.packet;

import lombok.Getter;

import java.util.UUID;

/** Send a player a message on whichever BungeeCord instance the UUID player is present on */
public class SendPlayerBungeeMessagePacket extends PlayerPacket {

    @Getter
    private final String formattedMessage;

    public SendPlayerBungeeMessagePacket(UUID playerTargeted, String formattedMessage) {
        super(playerTargeted);
        this.formattedMessage = formattedMessage;
    }
}
