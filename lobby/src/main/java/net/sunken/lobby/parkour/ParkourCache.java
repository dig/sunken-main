package net.sunken.lobby.parkour;

import com.google.common.collect.Sets;
import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.parkour.ParkourRedisHelper;
import net.sunken.common.player.PlayerRank;
import net.sunken.lobby.LobbyPlugin;
import net.sunken.lobby.player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ParkourCache {

    @Getter
    private List<Parkour> parkours;
    private Configuration config;

    private HashMap<String, Set<ParkourData>> bestTimeCache;

    public ParkourCache(Configuration config){
        this.parkours = new ArrayList<Parkour>();
        this.config = config;
        this.bestTimeCache = new HashMap<>();

        this.loadParkours();
        this.updateBestTimes();
    }

    private void loadParkours(){
        ConfigurationSection all = this.config.getConfigurationSection("parkours");

        for(String id : all.getKeys(false)){
            ConfigurationSection parkour = this.config.getConfigurationSection("parkours." + id);

            Material mainBlock = Material.valueOf(parkour.getString("mainMaterial"));
            Location start = this.parseCoordinates(parkour.getConfigurationSection("start"));
            Location end = this.parseCoordinates(parkour.getConfigurationSection("end"));
            Location resetPoint = this.parseCoordinates(parkour.getConfigurationSection("resetPoint"));

            List<Location> checkpoints = new ArrayList<>();
            for(String index : parkour.getConfigurationSection("checkpoints").getKeys(false)){
                checkpoints.add(this.parseCoordinates(parkour.getConfigurationSection("checkpoints." + index)));
            }

            ArrayList<Material> allowedMaterials = new ArrayList<>();
            for(String matName : parkour.getStringList("allowedMaterials")){
                allowedMaterials.add(Material.valueOf(matName));
            }

            if (parkour.contains("leaderboard") && parkour.contains("leaderboardPos")) {
                boolean leaderboard = parkour.getBoolean("leaderboard");
                Location leaderboardPos = this.parseCoordinates(parkour.getConfigurationSection("leaderboardPos"));

                parkours.add(new Parkour(id, mainBlock, allowedMaterials, start, end, checkpoints, resetPoint, true,
                        leaderboard, leaderboardPos));
            } else {
                parkours.add(new Parkour(id, mainBlock, allowedMaterials, start, end, checkpoints, resetPoint, true));
            }
        }
    }

    public void cleanupParkours(){
        for(Parkour parkour : this.parkours){
            parkour.cleanup();
        }
    }

    private Location parseCoordinates(ConfigurationSection section){
        String worldName = Bukkit.getWorlds().get(0).getName();
        if(section.contains("world")){
            worldName = section.getString("world");
        }

        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");

        double yaw = 0;
        if(section.contains("yaw")){
            yaw = section.getDouble("yaw");
        }

        double pitch = 0;
        if(section.contains("pitch")){
            pitch = section.getDouble("pitch");
        }

        return new Location(Bukkit.getWorld(worldName), x, y, z, (float) yaw, (float) pitch);
    }

    public Parkour getCurrentParkour(LobbyPlayer player){
        for(Parkour parkour : this.parkours){
            if(parkour.getPlayers().containsKey(player.getUUID())){
                return parkour;
            }
        }

        return null;
    }

    public List<ParkourData> getBestTimes(String id){
        if(this.bestTimeCache.containsKey(id)){
            List<ParkourData> sorted = this.bestTimeCache.get(id)
                    .stream()
                    .sorted(Comparator.comparing(ParkourData::getTime))
                    .collect(Collectors.toList());

            return sorted;
        }

        return new ArrayList<>();
    }

    public void updateBestTimes(){
        CompletableFuture.runAsync(() -> {
            this.bestTimeCache = new HashMap<>();

            RedisConnection redisConnection = Common.getInstance().getRedis();
            Jedis jedis = redisConnection.getConnection();

            try {
                ScanParams params = new ScanParams();
                params.count(100);
                params.match(ParkourRedisHelper.PARKOUR_STORAGE_KEY + ":*");

                ScanResult<String> scanResult = jedis.scan("0", params);
                List<String> keys = scanResult.getResult();

                for (String key : keys) {
                    Map<String, String> kv = jedis.hgetAll(key);

                    String uuid = kv.get(ParkourRedisHelper.PARKOUR_UUID_KEY);
                    String name = kv.get(ParkourRedisHelper.PARKOUR_NAME_KEY);
                    String rankStr = kv.get(ParkourRedisHelper.PARKOUR_RANK_KEY);
                    String type = kv.get(ParkourRedisHelper.PARKOUR_TYPE_KEY);
                    String timeStr = kv.get(ParkourRedisHelper.PARKOUR_TIME_KEY);

                    PlayerRank rank = PlayerRank.valueOf(rankStr);
                    Long time = Long.parseLong(timeStr);

                    if (this.bestTimeCache.get(type) == null) {
                        this.bestTimeCache.put(type, Sets.newLinkedHashSet());
                    }
                    this.bestTimeCache.get(type).add(new ParkourData(uuid, name, rank, type, time));
                }

            } catch (Exception e) {
                redisConnection.getJedisPool().returnBrokenResource(jedis);
            } finally {
                redisConnection.getJedisPool().returnResource(jedis);
            }
        }).thenRun(() -> {
            new BukkitRunnable(){

                @Override
                public void run(){
                    ParkourCache cache = LobbyPlugin.getInstance().getParkourCache();
                    for(Parkour parkour : cache.getParkours()){
                        parkour.updateLeaderboard();
                    }
                }

            }.runTask(LobbyPlugin.getInstance());
        });
    }
}
