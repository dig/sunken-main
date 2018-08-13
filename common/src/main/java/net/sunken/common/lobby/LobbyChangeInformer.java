package net.sunken.common.lobby;

import com.google.common.collect.ImmutableMap;
import net.sunken.common.Common;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.util.AsyncHelper;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import static net.sunken.common.lobby.LobbyRedisHelper.LOBBY_CACHE_CHANNEL;
import static net.sunken.common.lobby.LobbyRedisHelper.UPDATE_LOBBY_CACHE;

public class LobbyChangeInformer {

    private final RedisConnection redisConnection;

    public LobbyChangeInformer(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    public void inform(LobbyInfo lobbyInfo) {
        AsyncHelper.executor().submit(() -> {
            Jedis jedis = redisConnection.getConnection();
            try {
                jedis.hmset(LobbyRedisHelper.LOBBY_INFO_STORAGE_KEY + ":" + lobbyInfo.getServerName(),
                        ImmutableMap.of(
                                LobbyRedisHelper.SERVER_NAME_KEY, lobbyInfo.getServerName(),
                                LobbyRedisHelper.PLAYER_COUNT_KEY, lobbyInfo.getPlayerCount() + "",
                                LobbyRedisHelper.SERVER_IP_KEY, lobbyInfo.getServerIp(),
                                LobbyRedisHelper.SERVER_PORT_KEY, lobbyInfo.getServerPort() + ""
                        ));
                Common.getInstance().getRedis().sendRedisMessage(LOBBY_CACHE_CHANNEL, UPDATE_LOBBY_CACHE);
            } catch (Exception e) {
                redisConnection.getJedisPool().returnBrokenResource(jedis);
            } finally {
                redisConnection.getJedisPool().returnResource(jedis);
            }
        });
    }

    public CompletableFuture remove(LobbyInfo lobbyInfo) {
        return CompletableFuture.runAsync(() -> {
            Jedis jedis = redisConnection.getConnection();
            try {
                jedis.del(LobbyRedisHelper.LOBBY_INFO_STORAGE_KEY + ":" + lobbyInfo.getServerName());

                Common.getLogger().log(Level.INFO, "Updating all");
                Common.getInstance().getRedis().sendRedisMessage(LOBBY_CACHE_CHANNEL, UPDATE_LOBBY_CACHE);
            } catch (Exception e) {
                redisConnection.getJedisPool().returnBrokenResource(jedis);
            } finally {
                redisConnection.getJedisPool().returnResource(jedis);
            }
        }, AsyncHelper.executor());
    }
}
