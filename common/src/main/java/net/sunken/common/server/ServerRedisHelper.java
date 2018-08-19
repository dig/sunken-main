package net.sunken.common.server;

public class ServerRedisHelper {

    public static final String SERVER_STORAGE_KEY = "servers";

    public static final String SERVER_NAME_KEY = "name";
    public static final String SERVER_TYPE_KEY = "type";
    public static final String MAX_PLAYER_KEY = "max_players";
    public static final String PLAYER_COUNT_KEY = "player_count";
    public static final String SERVER_IP_KEY = "ip";
    public static final String SERVER_PORT_KEY = "port";
    public static final String SERVER_CREATED_KEY = "created";

    private ServerRedisHelper() {
        throw new AssertionError("You must not attempt to instantiate this class.");
    }
}
