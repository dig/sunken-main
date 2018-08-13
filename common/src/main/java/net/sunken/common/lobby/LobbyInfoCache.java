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
import java.util.logging.Level;
import java.util.logging.Logger;

public class LobbyInfoCache {

    private static Logger logger = Logger.getLogger(LobbyInfoCache.class.getName());

    private Set<LobbyInfo> cache = ImmutableSet.of();

    private final RedisConnection redisConnection;

    public LobbyInfoCache(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    public Set<LobbyInfo> getCache() {
        return this.cache;
    }

    public void updateCache() {
        logger.log(Level.INFO, "updateCache() called");

        AsyncHelper.executor().submit(() -> {
            try (Jedis jedis = redisConnection.getConnection()) {
                Set<LobbyInfo> updatedCache = Sets.newHashSet();

                ScanParams params = new ScanParams();
                params.match(LobbyRedisHelper.LOBBY_INFO_STORAGE_KEY + ":*");
                ScanResult<String> scanResult = jedis.scan("0", params);
                List<String> keys = scanResult.getResult();

                logger.log(Level.INFO, "Attempting to update local lobby cache");

                for (String key : keys) {
                    Map<String, String> kv = jedis.hgetAll(key); // key-value pairs of data stored with this key

                    String serverName = kv.get(LobbyRedisHelper.SERVER_NAME_KEY);
                    String playerCountStr = kv.get(LobbyRedisHelper.PLAYER_COUNT_KEY);
                    String serverIp = kv.get(LobbyRedisHelper.SERVER_IP_KEY);
                    String serverPortStr = kv.get(LobbyRedisHelper.SERVER_PORT_KEY);
                    int playerCount = Integer.parseInt(playerCountStr);
                    int serverPort = Integer.parseInt(serverPortStr);

                    LobbyInfo lobbyInfo = new LobbyInfo(serverName, playerCount, serverIp, serverPort);
                    updatedCache.add(lobbyInfo);

                    logger.log(Level.INFO, "Adding " + serverName + " count: " + playerCount + " IP: " + serverIp + " to local lobby cache");
                }

                this.cache = updatedCache;
            }
        });
    }
}
