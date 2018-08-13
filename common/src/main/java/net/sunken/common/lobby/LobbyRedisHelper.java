package net.sunken.common.lobby;

public final class LobbyRedisHelper {

    public static final String LOBBY_INFO_STORAGE_KEY = "lobby_info";
    public static final String SERVER_NAME_KEY = "server_name";
    public static final String PLAYER_COUNT_KEY = "player_count";
    public static final String SERVER_IP_KEY = "server_ip";
    public static final String SERVER_PORT_KEY = "server_port";

    public static final String LOBBY_CACHE_CHANNEL = "lobby_cache";
    public static final String UPDATE_LOBBY_CACHE = "UPDATE_LOBBY_CACHE";

    private LobbyRedisHelper() {
        throw new AssertionError("You must not attempt to instantiate this class.");
    }
}
