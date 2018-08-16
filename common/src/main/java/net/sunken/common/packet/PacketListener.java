package net.sunken.common.packet;

import net.sunken.common.Common;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;

public class PacketListener extends BinaryJedisPubSub {

    static final byte[] PACKET_CHANNEL = "SUNKEN_PACKET_CHANNEL".getBytes();

    private final Jedis subscriberJedis;

    private Map<Class<? extends Packet>, PacketHandler> handlers = PacketHandlerRegistry.getHandlers();

    public PacketListener(Jedis subscriberJedis) {
        this.subscriberJedis = subscriberJedis;
    }

    public void start() {
        new Thread(() -> subscriberJedis.subscribe(this, PACKET_CHANNEL)).start();
    }

    @Override
    public void onMessage(byte[] channel, byte[] message) {
        if (Arrays.equals(channel, PACKET_CHANNEL)) {
            Packet deserialized = Packet.fromBytes(message);

            if (deserialized != null && handlers.containsKey(deserialized.getClass())) {
                PacketHandler handler = handlers.get(deserialized.getClass());
                Common.getLogger().log(Level.INFO, " onMessage() handler: " + deserialized.getClass());

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
