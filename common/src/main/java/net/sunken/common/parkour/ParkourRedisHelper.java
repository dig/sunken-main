package net.sunken.common.parkour;

public class ParkourRedisHelper {

    public static final String PARKOUR_STORAGE_KEY = "parkour";

    public static final String PARKOUR_NAME_KEY = "name";
    public static final String PARKOUR_UUID_KEY = "uuid";
    public static final String PARKOUR_RANK_KEY = "rank";
    public static final String PARKOUR_TYPE_KEY = "type";
    public static final String PARKOUR_TIME_KEY = "time";

    private ParkourRedisHelper() {
        throw new AssertionError("You must not attempt to instantiate this class.");
    }
}
