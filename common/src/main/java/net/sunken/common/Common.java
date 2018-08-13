package net.sunken.common;

import lombok.Getter;
import net.sunken.common.database.MongoConnection;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.lobby.LobbyCacheUpdater;
import net.sunken.common.lobby.LobbyInfoCache;
import net.sunken.common.lobby.LobbyOnlineInformer;
import net.sunken.common.type.ServerType;

public class Common {

    @Getter
    private ServerType type;

    @Getter
    public static Common instance;

    @Getter
    private MongoConnection mongo;
    @Getter
    private RedisConnection redis;

    private LobbyInfoCache lobbyInfoCache;
    private LobbyOnlineInformer lobbyOnlineInformer;

    public void onCommonLoad(ServerType type) {
        instance = this;

        this.type = type;

        this.mongo = new MongoConnection(
                "***REMOVED***",
                19802,
                "admin",
                "***REMOVED***"
        );

        this.redis = new RedisConnection(
                "***REMOVED***",
                11640,
                "***REMOVED***"
        );

        if (type == ServerType.BUNGEECORD || type == ServerType.MAIN_LOBBY || type == ServerType.LOBBY) {
            lobbyInfoCache = new LobbyInfoCache(this.redis);
            LobbyCacheUpdater lobbyCacheUpdater = new LobbyCacheUpdater(redis.getConnection(), lobbyInfoCache);
            lobbyCacheUpdater.start();
            lobbyOnlineInformer = new LobbyOnlineInformer(this.redis);
        }
    }

    public LobbyInfoCache getLobbyInfoCache() {
        if (lobbyInfoCache == null) {
            throw new UnsupportedOperationException("the lobby server cache is unavailable, are you sure this is supported for the server type?");
        }
        return lobbyInfoCache;
    }

    public LobbyOnlineInformer getLobbyOnlineInformer() {
        if (lobbyOnlineInformer == null) {
            throw new UnsupportedOperationException("the lobby online informer is unavailable, are you sure this is supported for the server type?");
        }
        return lobbyOnlineInformer;
    }

    public void onCommonDisable() {
    }
}
