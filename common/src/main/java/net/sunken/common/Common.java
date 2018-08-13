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

    @Getter
    private LobbyInfoCache lobbyInfoCache;
    @Getter
    private LobbyOnlineInformer lobbyOnlineInformer;

    public void onCommonLoad(ServerType type) {
        instance = this;

        this.type = type;

        this.mongo = new MongoConnection("localhost", 27017, "username", "password");
        this.redis = new RedisConnection("localhost", 3306, "password");

        lobbyInfoCache = new LobbyInfoCache(this.redis);
        LobbyCacheUpdater lobbyCacheUpdater = new LobbyCacheUpdater(redis.getConnection(), lobbyInfoCache);
        lobbyCacheUpdater.start();
        lobbyOnlineInformer = new LobbyOnlineInformer(this.redis);
    }

    public void onCommonDisable() {
    }
}
