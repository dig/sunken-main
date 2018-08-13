package net.sunken.common;

import lombok.Getter;
import net.sunken.common.database.MongoConnection;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.lobby.LobbyCacheUpdater;
import net.sunken.common.lobby.LobbyInfoCache;
import net.sunken.common.lobby.LobbyOnlineInformer;
import net.sunken.common.type.ServerType;

public class Common {

    private boolean loaded = false;

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

        this.mongo = new MongoConnection("localhost", 27017, "username", "password");
        this.redis = new RedisConnection("localhost", 3306, "password");

        if (type == ServerType.BUNGEECORD || type == ServerType.MAIN_LOBBY || type == ServerType.LOBBY) {
            lobbyInfoCache = new LobbyInfoCache(this.redis);
            LobbyCacheUpdater lobbyCacheUpdater = new LobbyCacheUpdater(redis.getConnection(), lobbyInfoCache);
            lobbyCacheUpdater.start();
            lobbyOnlineInformer = new LobbyOnlineInformer(this.redis);
        }

        this.loaded = true;
    }

    public LobbyInfoCache getLobbyInfoCache() {
        if (loaded && lobbyInfoCache == null) {
            throw new UnsupportedOperationException("the lobby server cache is unavailable, are you sure this is supported for the server type?");
        }
        return lobbyInfoCache;
    }

    public LobbyOnlineInformer getLobbyOnlineInformer() {
        if (loaded && lobbyOnlineInformer == null) {
            throw new UnsupportedOperationException("the lobby online informer is unavailable, are you sure this is supported for the server type?");
        }
        return lobbyOnlineInformer;
    }

    public void onCommonDisable() {
    }
}
