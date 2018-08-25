package net.sunken.common.parties.packet.request;

import java.util.UUID;

/** Sent when a player wants to summon party members to their server */
public class MPartySummonRequestPacket extends MPartyRequestPacket {

    public MPartySummonRequestPacket(UUID requesting) {
        super(requesting);
    }
}
