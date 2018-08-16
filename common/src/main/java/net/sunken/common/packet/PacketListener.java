package net.sunken.common.packet;

import redis.clients.jedis.BinaryJedisPubSub;

import java.util.Arrays;
import java.util.Map;

public class PacketListener extends BinaryJedisPubSub {

    private Map<Class<? extends Packet>, PacketHandler> handlers = PacketHandlerRegistry.getHandlers();
    public static final byte[] PACKET_CHANNEL = "SUNKEN_PACKET_CHANNEL".getBytes();

    @Override
    public void onMessage(byte[] channel, byte[] message) {
        if (Arrays.equals(channel, PACKET_CHANNEL)) {
            Packet deserialized = Packet.fromBytes(message);
            if (deserialized != null) {
                PacketHandler handler = handlers.get(deserialized.getClass());
                handler.onReceive(deserialized);
            }
        }
    }

    @Override
    public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
    }

    @Override
    public void onSubscribe(byte[] channel, int subscribedChannels) {
    }

    @Override
    public void onUnsubscribe(byte[] channel, int subscribedChannels) {
    }

    @Override
    public void onPUnsubscribe(byte[] pattern, int subscribedChannels) {
    }

    @Override
    public void onPSubscribe(byte[] pattern, int subscribedChannels) {
    }
}
