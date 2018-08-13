package net.sunken.common.lobby;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LobbyCacheUpdater {

    private static Logger logger = Logger.getLogger(LobbyCacheUpdater.class.getName());

    private final Jedis subscriberJedis;
    private final LobbyInfoCache lobbyInfoCache;

    public LobbyCacheUpdater(Jedis subscriberJedis, LobbyInfoCache lobbyInfoCache) {
        this.subscriberJedis = subscriberJedis;
        this.lobbyInfoCache = lobbyInfoCache;
    }

    public void start() {
        new Thread(() -> {
            subscriberJedis.subscribe(new Listener(), LobbyRedisHelper.LOBBY_CACHE_CHANNEL);
        }).start();
    }

    private class Listener extends JedisPubSub {

        @Override
        public void onMessage(String channel, String message) {
            logger.log(Level.INFO, "onMessage() called - " + channel + " - " + message);

            if (channel.equals(LobbyRedisHelper.LOBBY_CACHE_CHANNEL)) {
                if (message.equals(LobbyRedisHelper.UPDATE_LOBBY_CACHE)) {
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
