package net.sunken.common.parties.packet.request;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

/**
 * Sent when a player does /party list requesting a list of party members
 */
public class MPartyListRequestPacket extends Packet {

    @Getter
    private UUID requester;

    @Getter
    private Boolean chat;

    public MPartyListRequestPacket(UUID requester, Boolean chat) {
        this.requester = requester;
        this.chat = chat;
    }
}
