package me.minigames.common.database;

import me.minigames.MinigamePlugin;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.logging.Level;

public class RedisConnection extends Database<Jedis> {
    private static MinigamePlugin plugin = MinigamePlugin.getInstance();

    private JedisPool jedisPool;
    private Jedis jedisSubscriber;

    /*
     * Used if creating a connection without password
     */
    public RedisConnection(String host, int port) {
        this(host, port, "");
    }

    /*
     * Authenticating with a password
     */
    public RedisConnection(String host, int port, String password) {
        Runnable redis = () -> {
            JedisPoolConfig poolCfg = new JedisPoolConfig();
            jedisPool = new JedisPool(poolCfg, host, port, 0, password);
        };
        Bukkit.getScheduler().runTaskAsynchronously(plugin, redis);
    }

    public void sendRedisMessage(final String channel, final String message) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (jedisPool != null) {
                Jedis jedis = jedisPool.getResource();

                try {
                    jedis.publish(channel, message);
                } catch (Exception e) {
                    jedisPool.returnBrokenResource(jedis);
                } finally {
                    jedisPool.returnResource(jedis);
                }
            } else {
                Bukkit.getLogger().log(Level.SEVERE, "Unable to send Redis message");
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

    public void returnConnection(Jedis jedis){
        jedisPool.returnResource(jedis);
    }
}
