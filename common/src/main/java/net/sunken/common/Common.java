package net.sunken.common;

import lombok.Getter;
import net.sunken.common.achievements.AchievementRegistry;
import net.sunken.common.achievements.NetworkFirstJoinAchievement;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.database.MongoConnection;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.packet.PacketHandlerRegistry;
import net.sunken.common.packet.PacketListener;
import net.sunken.common.server.ServerCacheUpdater;
import net.sunken.common.server.ServerChangeInformer;
import net.sunken.common.server.data.ServerObject;
import net.sunken.common.server.ServerObjectCache;
import net.sunken.common.server.packet.ServerCacheUpdatePacket;
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

    @Getter
    private DataManager dataManager;

    private ServerObjectCache serverObjectCache;
    private ServerChangeInformer serverChangeInformer;

    public void onCommonLoad(boolean listenForServers) {
        this.mongo = new MongoConnection(
                "",
                0,
                "admin",
                "",
                DatabaseConstants.DATABASE_NAME
        );

        this.redis = new RedisConnection(
                "",
                0,
                ""
        );

        this.dataManager = new DataManager();

        PacketListener packetListener = new PacketListener(redis.getConnection());
        packetListener.start();

        // Should the server keep track of other servers?
        if (listenForServers) {
            serverObjectCache = new ServerObjectCache(this.redis);
            PacketHandlerRegistry.registerHandler(ServerCacheUpdatePacket.class, new ServerCacheUpdater(this.serverObjectCache));
        }
        serverChangeInformer = new ServerChangeInformer(this.redis);

        AchievementRegistry.addAchievement(new NetworkFirstJoinAchievement());

        this.loaded = true;
    }

    public void onCommonLoad(boolean listenForServers,
                             ServerType serverType, int maxPlayers, int serverPort) {
        this.onCommonLoad(listenForServers);

        // Add server to the network
        ServerInstance.setInstance(new ServerInstance(serverType, maxPlayers, 0, serverPort));

        // Add shutdown hook for when server closes
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ServerObject serverObject = ServerInstance.instance().getServerObject();
            Common.getInstance().getServerChangeInformer().removeSync(serverObject);

            Common.getInstance().onCommonDisable();
        }));
    }

    public ServerObjectCache getServerCache() {
        if (loaded && serverObjectCache == null) {
            throw new UnsupportedOperationException(
                    "the server cache is unavailable, are you sure this is supported for the server type?");
        }
        return serverObjectCache;
    }

    public ServerChangeInformer getServerChangeInformer() {
        if (loaded && serverChangeInformer == null) {
            throw new UnsupportedOperationException(
                    "the online informer is unavailable, are you sure this is supported for the server type?");
        }
        return serverChangeInformer;
    }

    public void onCommonDisable() {
        this.mongo.disconnect();
        this.redis.disconnect();
    }

    private Common() {
    }
}
