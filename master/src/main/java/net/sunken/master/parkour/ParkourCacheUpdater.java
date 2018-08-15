package net.sunken.master.parkour;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import net.sunken.common.Common;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.master.Master;
import org.bson.Document;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.logging.Level;

import static com.mongodb.client.model.Filters.eq;

public class ParkourCacheUpdater {

    private final Jedis subscriberJedis;

    public ParkourCacheUpdater(Jedis subscriberJedis) {
        this.subscriberJedis = subscriberJedis;
    }

    public void start() {
        new Thread(() -> {
            subscriberJedis.subscribe(new Listener(), ParkourRedisHelper.PARKOUR_CACHE_CHANNEL);
        }).start();
    }

    private class Listener extends JedisPubSub {

        @Override
        public void onMessage(String channel, String message) {
            if (channel.equals(ParkourRedisHelper.PARKOUR_CACHE_CHANNEL)) {
                if (message.equals(ParkourRedisHelper.UPDATE_PARKOUR_CACHE)) {

                    Master.getInstance().getParkourCache().updateCache("test").thenRun(() -> {
                        Common.getInstance().getRedis().sendRedisMessage(ParkourRedisHelper.PARKOUR_CACHE_CHANNEL,
                                ParkourRedisHelper.UPDATE_PARKOUR_LEADERBOARD);
                    });

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
