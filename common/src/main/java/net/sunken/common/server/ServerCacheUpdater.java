package net.sunken.common.server;

import net.sunken.common.Common;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.logging.Level;

public class ServerCacheUpdater {

    private final Jedis subscriberJedis;
    private final ServerObjectCache serverObjectCache;

    public ServerCacheUpdater(Jedis subscriberJedis, ServerObjectCache serverObjectCache) {
        this.subscriberJedis = subscriberJedis;
        this.serverObjectCache = serverObjectCache;
    }

    public void start() {
        new Thread(() -> {
            subscriberJedis.subscribe(new Listener(), ServerRedisHelper.SERVER_CACHE_CHANNEL);
        }).start();
    }

    private class Listener extends JedisPubSub {

        @Override
        public void onMessage(String channel, String message) {
            Common.getLogger().log(Level.INFO, "onMessage() called - " + channel + " - " + message);

            if (channel.equals(ServerRedisHelper.SERVER_CACHE_CHANNEL)) {
                if (message.equals(ServerRedisHelper.UPDATE_SERVER_CACHE)) {
                    serverObjectCache.updateCache();
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
