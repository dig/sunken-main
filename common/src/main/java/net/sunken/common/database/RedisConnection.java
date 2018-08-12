package net.sunken.common.database;

import com.sun.istack.internal.Nullable;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection extends Database<Jedis> {

    @Getter
    private JedisPool jedisPool;

    /**
     * Use to authenticate with a password.
     *
     * @param host     the host IP of the Redis instance
     * @param port     the port the Redis instance is running on
     * @param password the password of the Redis instance if applicable, can be null
     */
    public RedisConnection(String host, int port, @Nullable String password) {
        JedisPoolConfig poolCfg = new JedisPoolConfig();
        jedisPool = new JedisPool(poolCfg, host, port, 0, password);
    }

    public void sendRedisMessage(final String channel, final String message) {
        this.runAsync(() -> {
            if (jedisPool != null) {
                Jedis jedis = jedisPool.getResource();

                try {
                    jedis.publish(channel, message);
                } catch (Exception e) {
                    jedisPool.returnBrokenResource(jedis);
                } finally {
                    jedisPool.returnResource(jedis);
                }
            }
        });
    }

    @Override
    public Jedis getConnection() {
        return jedisPool.getResource();
    }

    @Override
    public void disconnect() {
        jedisPool.destroy();
    }
}
