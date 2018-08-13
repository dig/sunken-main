package net.sunken.common.lobby;

import com.google.common.collect.ImmutableMap;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.util.AsyncHelper;
import redis.clients.jedis.Jedis;

public class LobbyOnlineInformer {

    private final RedisConnection redisConnection;

    public LobbyOnlineInformer(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    public void informOfNewLobby(LobbyInfo lobbyInfo) {
        AsyncHelper.executor().submit(() -> {
            try (Jedis jedis = redisConnection.getConnection()) {
                jedis.hmset(LobbyRedisConstants.LOBBY_INFO_STORAGE_KEY + ":" + lobbyInfo.getServerName(),
                            ImmutableMap.of(
                                    LobbyRedisConstants.SERVER_NAME_KEY, lobbyInfo.getServerName(),
                                    LobbyRedisConstants.PLAYER_COUNT_KEY, lobbyInfo.getPlayerCount() + ""
                            ));
            }
        });
    }
}
