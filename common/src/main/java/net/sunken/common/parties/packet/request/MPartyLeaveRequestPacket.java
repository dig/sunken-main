package net.sunken.common.parties.packet.request;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

/**
 * Sent when a player does /party leave
 */
public class MPartyLeaveRequestPacket extends Packet {

    @Getter
    private final UUID leaver;
    @Getter
    private final boolean isDisbanding;

    public MPartyLeaveRequestPacket(UUID leaver, boolean isDisbanding) {
        this.leaver = leaver;
        this.isDisbanding = isDisbanding;
    }
}
