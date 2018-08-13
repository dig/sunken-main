package net.sunken.common.lobby;

import net.sunken.common.util.AsyncHelper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class LobbyCacheUpdater {

    private final Jedis subscriberJedis;
    private final LobbyInfoCache lobbyInfoCache;

    public LobbyCacheUpdater(Jedis subscriberJedis, LobbyInfoCache lobbyInfoCache) {
        this.subscriberJedis = subscriberJedis;
        this.lobbyInfoCache = lobbyInfoCache;
    }

    public void start() {
        AsyncHelper.executor().submit(() -> {
            new Thread(() -> {
                subscriberJedis.subscribe(new Listener(), LobbyRedisConstants.LOBBY_CACHE_CHANNEL);
            }).start();
        });
    }

    public class Listener extends JedisPubSub {

        @Override
        public void onMessage(String channel, String message) {
            if (channel.equals(LobbyRedisConstants.LOBBY_CACHE_CHANNEL)) {
                if (message.equals(LobbyRedisConstants.UPDATE_LOBBY_CACHE)) {
                    lobbyInfoCache.updateCache();
                }
            }
        }

        @Override
        public void onSubscribe(String channel, int subscribedChannels) {
        }

        @Override
        public void onUnsubscribe(String channel, int subscribedChannels) {
        }

        @Override
        public void onPSubscribe(String pattern, int subscribedChannels) {
        }

        @Override
        public void onPUnsubscribe(String pattern, int subscribedChannels) {
        }

        @Override
        public void onPMessage(String pattern, String channel, String message) {
        }
    }
}