package net.sunken.common.parties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sunken.common.packet.Packet;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class PartyInviteSendPacket extends Packet {

    @Getter
    @Setter
    private UUID uuid;
    @Getter
    @Setter
    private UUID to;
}
