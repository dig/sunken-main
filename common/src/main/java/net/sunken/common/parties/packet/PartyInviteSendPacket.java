package net.sunken.common.parties.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

/**
 * Sent when a player does /party (player) indicating
 * a party request was sent.
 */
public class PartyInviteSendPacket extends Packet {

    @Getter
    private UUID creator;
    @Getter
    private String toInvite;

    public PartyInviteSendPacket(UUID creator, String toInvite) {
        this.creator = creator;
        this.toInvite = toInvite;
    }
}
