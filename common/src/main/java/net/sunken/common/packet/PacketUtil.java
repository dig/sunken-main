package net.sunken.common.packet;

import net.sunken.common.Common;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.util.AsyncHelper;
import redis.clients.jedis.Jedis;

public final class PacketUtil {

    private static RedisConnection redisConnection = Common.getInstance().getRedis();

    public static void sendPacket(Packet packet) {
        AsyncHelper.executor().submit(() -> {
            Jedis jedis = redisConnection.getConnection();
            try {
                jedis.publish(PacketListener.PACKET_CHANNEL, packet.toBytes());
            } catch (Exception e) {
                redisConnection.getJedisPool().returnBrokenResource(jedis);
            } finally {
                redisConnection.getJedisPool().returnResource(jedis);
            }
        });
    }

    private PacketUtil() {
    }
}
