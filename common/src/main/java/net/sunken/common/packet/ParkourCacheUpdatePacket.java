package net.sunken.common.packet;

public class ParkourCacheUpdatePacket extends Packet {

    public static class Handler extends PacketHandler<ParkourCacheUpdatePacket> {

        @Override
        public void onReceive(ParkourCacheUpdatePacket packet) {
            // do something with this packet
        }
    }
}
