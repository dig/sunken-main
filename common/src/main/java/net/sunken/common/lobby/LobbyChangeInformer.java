package net.sunken.common.lobby;

import com.google.common.collect.ImmutableMap;
import net.sunken.common.Common;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.util.AsyncHelper;
import redis.clients.jedis.Jedis;

import java.util.logging.Level;
import java.util.logging.Logger;

import static net.sunken.common.lobby.LobbyRedisHelper.LOBBY_CACHE_CHANNEL;
import static net.sunken.common.lobby.LobbyRedisHelper.UPDATE_LOBBY_CACHE;

public class LobbyChangeInformer {

    private static Logger logger = Logger.getLogger(LobbyChangeInformer.class.getName());
    private final RedisConnection redisConnection;

    public LobbyChangeInformer(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    public void inform(LobbyInfo lobbyInfo) {
        logger.log(Level.INFO, "inform() called");

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
            } catch (Exception e) {
                redisConnection.getJedisPool().returnBrokenResource(jedis);
            } finally {
                redisConnection.getJedisPool().returnResource(jedis);
            }

            /* try (Jedis jedis = redisConnection.getConnection()) {
                jedis.hmset(LobbyRedisHelper.LOBBY_INFO_STORAGE_KEY + ":" + lobbyInfo.getServerName(),
                            ImmutableMap.of(
                                    LobbyRedisHelper.SERVER_NAME_KEY, lobbyInfo.getServerName(),
                                    LobbyRedisHelper.PLAYER_COUNT_KEY, lobbyInfo.getPlayerCount() + "",
                                    LobbyRedisHelper.SERVER_IP_KEY, lobbyInfo.getServerIp(),
                                    LobbyRedisHelper.SERVER_PORT_KEY, lobbyInfo.getServerPort() + ""
                            ));

                Common.getInstance().getRedis().sendRedisMessage(LOBBY_CACHE_CHANNEL, UPDATE_LOBBY_CACHE);
            } */
        });
    }

    public void remove(LobbyInfo lobbyInfo) {
        logger.log(Level.INFO, "remove() called");

        AsyncHelper.executor().submit(() -> {
            try (Jedis jedis = redisConnection.getConnection()) {
                jedis.del(LobbyRedisHelper.LOBBY_INFO_STORAGE_KEY + ":" + lobbyInfo.getServerName());

                Common.getInstance().getRedis().sendRedisMessage(LOBBY_CACHE_CHANNEL, UPDATE_LOBBY_CACHE);
            }
        });
    }
}
