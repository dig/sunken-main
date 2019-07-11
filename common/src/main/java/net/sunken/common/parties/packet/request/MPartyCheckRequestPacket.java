package net.sunken.common.parties.packet.request;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

public class MPartyCheckRequestPacket extends Packet {

    @Getter
    private UUID requester;

    public MPartyCheckRequestPacket(UUID requester) {
        this.requester = requester;
    }

}
