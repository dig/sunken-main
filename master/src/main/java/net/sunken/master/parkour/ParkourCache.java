package net.sunken.master.parkour;

import com.google.common.collect.ImmutableMap;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import net.sunken.common.Common;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.parkour.ParkourRedisHelper;
import org.bson.Document;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ParkourCache {

    private MongoCollection<Document> playerCollection;

    public ParkourCache (){
        this.playerCollection = Common.getInstance()
                .getMongo()
                .getConnection()
                .getDatabase(DatabaseConstants.DATABASE_NAME)
                .getCollection(DatabaseConstants.PLAYER_COLLECTION);
    }

    public CompletableFuture<Void> updateCache(String id){
        return CompletableFuture.runAsync(() -> {
            // Sort by parkour ID
            AggregateIterable aggregate = playerCollection.aggregate(Arrays.asList(
                    Aggregates.match(Filters.eq("parkours.id", id)),
                    Aggregates.sort(Sorts.ascending("parkours.time")),
                    Aggregates.limit(10)
            ));

            // Cache in redis for lobbies to read
            RedisConnection redisConnection = Common.getInstance().getRedis();
            Jedis jedis = redisConnection.getConnection();

            try {
                // Delete previous data
                ScanParams params = new ScanParams();
                params.match(ParkourRedisHelper.PARKOUR_STORAGE_KEY + ":" + id + ":*");
                ScanResult<String> scanResult = jedis.scan("0", params);
                List<String> keys = scanResult.getResult();

                jedis.del(keys.toArray(new String[keys.size()]));

                MongoCursor<Document> iterator = aggregate.iterator();
                while (iterator.hasNext()) {
                    Document next = iterator.next();

                    String uuid = next.getString("uuid");
                    String name = next.getString("name");
                    String rank = next.getString("rank");
                    Long time = Long.MAX_VALUE;

                    List<Document> parkours = next.get("parkours", List.class);
                    for(Document parkour : parkours){
                        if(parkour.getString("id").equals(id)){
                            time = parkour.getLong("time");
                        }
                    }

                    jedis.hmset(ParkourRedisHelper.PARKOUR_STORAGE_KEY + ":" + id + ":" + uuid, ImmutableMap.of(
                            ParkourRedisHelper.PARKOUR_UUID_KEY, uuid,
                            ParkourRedisHelper.PARKOUR_NAME_KEY, name,
                            ParkourRedisHelper.PARKOUR_RANK_KEY, rank,
                            ParkourRedisHelper.PARKOUR_TYPE_KEY, id,
                            ParkourRedisHelper.PARKOUR_TIME_KEY, time + ""
                    ));
                }
            } catch (Exception e) {
                redisConnection.getJedisPool().returnBrokenResource(jedis);
            } finally {
                redisConnection.getJedisPool().returnResource(jedis);
            }
        });
    }
}
