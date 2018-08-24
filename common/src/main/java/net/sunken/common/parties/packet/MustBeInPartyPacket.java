package net.sunken.common.parties.packet;

import net.sunken.common.packet.PlayerPacket;

import java.util.UUID;

public class MustBeInPartyPacket extends PlayerPacket {

    public MustBeInPartyPacket(UUID playerTargeted) {
        super(playerTargeted);
    }
}
