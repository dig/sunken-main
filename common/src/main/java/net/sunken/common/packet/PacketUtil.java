package net.sunken.common.packet;

import net.sunken.common.Common;
import net.sunken.common.database.RedisConnection;
import redis.clients.jedis.Jedis;

public final class PacketUtil {

    private static RedisConnection redisConnection = Common.getInstance().getRedis();

    public static void sendPacket(Packet packet) {
        Jedis connection = redisConnection.getConnection();
        connection.publish(PacketListener.PACKET_CHANNEL, packet.toBytes());
    }

    private PacketUtil() {
    }
}
