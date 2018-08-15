package net.sunken.master.parkour;

public class ParkourRedisHelper {

    public static final String PARKOUR_STORAGE_KEY = "parkour";

    public static final String PARKOUR_CACHE_CHANNEL = "parkour_cache";

    public static final String UPDATE_PARKOUR_CACHE = "UPDATE_PARKOUR_CACHE";
    public static final String UPDATE_PARKOUR_LEADERBOARD = "UPDATE_PARKOUR_LEADERBOARD";

    public static final String PARKOUR_NAME_KEY = "name";
    public static final String PARKOUR_UUID_KEY = "uuid";
    public static final String PARKOUR_RANK_KEY = "rank";
    public static final String PARKOUR_TIME_KEY = "time";

    private ParkourRedisHelper() {
        throw new AssertionError("You must not attempt to instantiate this class.");
    }

}
