package net.sunken.common.packet.custom;

import lombok.Getter;
import net.sunken.common.packet.Packet;

public class ParkourCacheUpdatePacket extends Packet {

    @Getter
    private String id;

}