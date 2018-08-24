package net.sunken.common.parties.packet;

import net.sunken.common.packet.PlayerPacket;

import java.util.UUID;

/** Sent by Master when an action is denied based on the notion a player must be in a party first */
public class MustBeInPartyPacket extends PlayerPacket {

    public MustBeInPartyPacket(UUID playerTargeted) {
        super(playerTargeted);
    }
}
