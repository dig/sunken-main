package net.sunken.common.packet;

import net.sunken.common.Common;
import net.sunken.common.server.ServerRedisHelper;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;

public class PacketUpdater {

    public static final byte[] PACKET_CHANNEL = "SUNKEN_PACKET_CHANNEL".getBytes();

    private final Jedis subscriberJedis;
    public PacketUpdater(Jedis subscriberJedis) {
        this.subscriberJedis = subscriberJedis;
    }

    public void start() {
        new Thread(() -> {
            subscriberJedis.subscribe(new PacketListener(), PACKET_CHANNEL);
        }).start();
    }

    private class PacketListener extends BinaryJedisPubSub {

        private Map<Class<? extends Packet>, PacketHandler> handlers = PacketHandlerRegistry.getHandlers();

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

}
