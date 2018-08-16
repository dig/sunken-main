package net.sunken.common.server;

import com.google.common.collect.ImmutableMap;
import net.sunken.common.Common;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.util.AsyncHelper;
import redis.clients.jedis.Jedis;

import static net.sunken.common.server.ServerRedisHelper.SERVER_CACHE_CHANNEL;
import static net.sunken.common.server.ServerRedisHelper.UPDATE_SERVER_CACHE;

public class ServerChangeInformer {

    private final RedisConnection redisConnection;

    public ServerChangeInformer(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    public void inform(ServerObject serverObject) {
        AsyncHelper.executor().submit(() -> {
            Jedis jedis = redisConnection.getConnection();
            try {
                ImmutableMap<String, String> serverKeys = ImmutableMap.<String, String>builder()
                        .put(ServerRedisHelper.SERVER_NAME_KEY, serverObject.getServerName())
                        .put(ServerRedisHelper.SERVER_TYPE_KEY, serverObject.getServerType().toString())
                        .put(ServerRedisHelper.MAX_PLAYER_KEY, serverObject.getMaxPlayers() + "")
                        .put(ServerRedisHelper.PLAYER_COUNT_KEY, serverObject.getPlayerCount() + "")
                        .put(ServerRedisHelper.SERVER_IP_KEY, serverObject.getServerIp())
                        .put(ServerRedisHelper.SERVER_PORT_KEY, serverObject.getServerPort() + "")
                        .put(ServerRedisHelper.SERVER_CREATED_KEY, serverObject.getCreated() + "")
                        .build();

                jedis.hmset(ServerRedisHelper.SERVER_STORAGE_KEY + ":" + serverObject.getServerName(), serverKeys);
                Common.getInstance().getRedis().sendRedisMessage(SERVER_CACHE_CHANNEL, UPDATE_SERVER_CACHE);
            } catch (Exception e) {
                redisConnection.getJedisPool().returnBrokenResource(jedis);
            } finally {
                redisConnection.getJedisPool().returnResource(jedis);
            }
        });
    }

    public void removeSync(ServerObject serverObject) {
        Jedis jedis = redisConnection.getConnection();

        try {
            jedis.del(ServerRedisHelper.SERVER_STORAGE_KEY + ":" + serverObject.getServerName());

            Common.getInstance().getRedis().sendRedisMessage(SERVER_CACHE_CHANNEL, UPDATE_SERVER_CACHE);
        } catch (Exception e) {
            redisConnection.getJedisPool().returnBrokenResource(jedis);
        } finally {
            redisConnection.getJedisPool().returnResource(jedis);
        }
    }
}

