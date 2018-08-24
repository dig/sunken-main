package net.sunken.common.parties.packet.request;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

/**
 * Sent when a player does /party (player) indicating
 * a party request was sent.
 */
public class MPartyInviteRequestPacket extends Packet {

    @Getter
    private UUID creator;
    @Getter
    private String invitee;

    public MPartyInviteRequestPacket(UUID creator, String invitee) {
        this.creator = creator;
        this.invitee = invitee;
    }
}
