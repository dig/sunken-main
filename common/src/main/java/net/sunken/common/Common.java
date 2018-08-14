package net.sunken.common;

import lombok.Getter;
import net.sunken.common.database.MongoConnection;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.lobby.LobbyCacheUpdater;
import net.sunken.common.lobby.LobbyChangeInformer;
import net.sunken.common.lobby.LobbyInfoCache;
import net.sunken.common.type.ServerType;

import java.util.logging.Logger;

public class Common {

    private boolean loaded = false;

    @Getter
    private static Common instance = new Common();
    @Getter
    private static Logger logger = Logger.getLogger(Common.class.getName());

    @Getter
    private MongoConnection mongo;
    @Getter
    private RedisConnection redis;

    private LobbyInfoCache lobbyInfoCache;
    private LobbyChangeInformer lobbyChangeInformer;

    public void onCommonLoad(boolean listenForLobbies) {
        this.mongo = new MongoConnection(
                "***REMOVED***",
                19802,
                "admin",
                "***REMOVED***",
                "sunken"
        );

        this.redis = new RedisConnection(
                "***REMOVED***",
                11640,
                "***REMOVED***"
        );

        if (listenForLobbies) {
            lobbyInfoCache = new LobbyInfoCache(this.redis);
            LobbyCacheUpdater lobbyCacheUpdater = new LobbyCacheUpdater(redis.getConnection(), lobbyInfoCache);
            lobbyCacheUpdater.start();
            lobbyChangeInformer = new LobbyChangeInformer(this.redis);
        }

        this.loaded = true;
    }

    public LobbyInfoCache getLobbyInfoCache() {
        if (loaded && lobbyInfoCache == null) {
            throw new UnsupportedOperationException("the lobby server cache is unavailable, are you sure this is supported for the server type?");
        }
        return lobbyInfoCache;
    }

    public LobbyChangeInformer getLobbyChangeInformer() {
        if (loaded && lobbyChangeInformer == null) {
            throw new UnsupportedOperationException("the lobby online informer is unavailable, are you sure this is supported for the server type?");
        }
        return lobbyChangeInformer;
    }

    public void onCommonDisable() {
        this.mongo.disconnect();
        this.redis.disconnect();
    }

    private Common() {
    }
}
