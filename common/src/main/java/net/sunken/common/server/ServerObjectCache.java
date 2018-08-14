package net.sunken.common.server;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.type.ServerType;
import net.sunken.common.util.AsyncHelper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerObjectCache {

    private Set<ServerObject> cache = ImmutableSet.of();

    private final RedisConnection redisConnection;

    public ServerObjectCache(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    public Set<ServerObject> getCache() { return this.cache; }

    public Set<ServerObject> getCache(ServerType serverType) {
        Set<ServerObject> sortedServers = this.cache.stream()
                .filter(server -> server.getServerType() == serverType)
                .collect(Collectors.toSet());

        return sortedServers;
    }


    public void updateCache() {
        AsyncHelper.executor().submit(() -> {
            Jedis jedis = redisConnection.getConnection();
            try {
                Set<ServerObject> updatedCache = Sets.newHashSet();

                ScanParams params = new ScanParams();
                params.match(ServerRedisHelper.SERVER_STORAGE_KEY + ":*");
                ScanResult<String> scanResult = jedis.scan("0", params);
                List<String> keys = scanResult.getResult();

                for (String key : keys) {
                    Map<String, String> kv = jedis.hgetAll(key); // key-value pairs of data stored with this key

                    String serverName = kv.get(ServerRedisHelper.SERVER_NAME_KEY);
                    String serverTypeStr = kv.get(ServerRedisHelper.SERVER_TYPE_KEY);
                    String maxPlayersStr = kv.get(ServerRedisHelper.MAX_PLAYER_KEY);
                    String playerCountStr = kv.get(ServerRedisHelper.PLAYER_COUNT_KEY);
                    String serverIp = kv.get(ServerRedisHelper.SERVER_IP_KEY);
                    String serverPortStr = kv.get(ServerRedisHelper.SERVER_PORT_KEY);

                    ServerType serverType = ServerType.valueOf(serverTypeStr);
                    int maxPlayers = Integer.parseInt(maxPlayersStr);
                    int playerCount = Integer.parseInt(playerCountStr);
                    int serverPort = Integer.parseInt(serverPortStr);

                    ServerObject serverObject = new ServerObject(serverName, serverType, maxPlayers, playerCount, serverIp, serverPort);
                    updatedCache.add(serverObject);
                }

                this.cache = updatedCache;
            } catch (Exception e) {
                redisConnection.getJedisPool().returnBrokenResource(jedis);
            } finally {
                redisConnection.getJedisPool().returnResource(jedis);
            }
        });
    }
}