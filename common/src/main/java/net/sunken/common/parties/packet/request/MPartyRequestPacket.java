package net.sunken.common.parties.packet.request;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

abstract class MPartyRequestPacket extends Packet {

    @Getter
    private final UUID requester;

    protected MPartyRequestPacket(UUID requester) {
        this.requester = requester;
    }
}
