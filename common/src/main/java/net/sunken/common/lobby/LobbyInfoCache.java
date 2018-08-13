package net.sunken.common.lobby;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.util.AsyncHelper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LobbyInfoCache {

    private Set<LobbyInfo> cache = ImmutableSet.of();

    private final RedisConnection redisConnection;

    public LobbyInfoCache(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    public Set<LobbyInfo> getCache() {
        return this.cache;
    }

    public void updateCache() {
        AsyncHelper.executor().submit(() -> {
            try (Jedis jedis = redisConnection.getConnection()) {
                Set<LobbyInfo> updatedCache = Sets.newHashSet();

                ScanParams params = new ScanParams();
                params.match(LobbyRedisConstants.LOBBY_INFO_STORAGE_KEY + ":*");
                ScanResult<String> scanResult = jedis.scan("0", params);
                List<String> keys = scanResult.getResult();

                for (String key : keys) {
                    Map<String, String> kv = jedis.hgetAll(key); // key-value pairs of data stored with this key
                    String serverName = kv.get(LobbyRedisConstants.SERVER_NAME_KEY);
                    String playerCountStr = kv.get(LobbyRedisConstants.PLAYER_COUNT_KEY);
                    int playerCount = Integer.parseInt(playerCountStr);
                    LobbyInfo lobbyInfo = new LobbyInfo(serverName, playerCount);
                    updatedCache.add(lobbyInfo);
                }

                this.cache = updatedCache;
            }
        });
    }
}