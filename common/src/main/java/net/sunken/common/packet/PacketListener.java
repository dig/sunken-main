package net.sunken.common.packet;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Map;

public class PacketListener {

    static final byte[] PACKET_CHANNEL = "SUNKEN_PACKET_CHANNEL".getBytes();

    private final Jedis subscriberJedis;

    public PacketListener(Jedis subscriberJedis) {
        this.subscriberJedis = subscriberJedis;
    }

    public void start() {
        new Thread(() -> subscriberJedis.subscribe(new Listener(), PACKET_CHANNEL)).start();
    }

    /** The publish/subscribe client */
    private class Listener extends BinaryJedisPubSub {

        private Map<Class<? extends Packet>, PacketHandler> handlers = PacketHandlerRegistry.getHandlers();

        @Override
        public void onMessage(byte[] channel, byte[] message) {
            if (Arrays.equals(channel, PACKET_CHANNEL)) {
                Packet deserialized = Packet.fromBytes(message);
                if (deserialized != null && handlers.containsKey(deserialized.getClass())) {
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
}