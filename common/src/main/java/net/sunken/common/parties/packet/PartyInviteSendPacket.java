package net.sunken.common.parties.packet;

import lombok.Getter;
import lombok.Setter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

public class PartyInviteSendPacket extends Packet {

    @Getter
    @Setter
    private UUID creator;
    @Getter
    @Setter
    private UUID to;

    public PartyInviteSendPacket(UUID creator, UUID to) {
        this.creator = creator;
        this.to = to;
    }

    public PartyInviteSendPacket() {
    }
}
