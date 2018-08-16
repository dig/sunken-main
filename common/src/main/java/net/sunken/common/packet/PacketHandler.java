package net.sunken.common.packet;

public abstract class PacketHandler<T extends Packet> {

    public abstract void onReceive(T packet);
}
