package net.sunken.common.server;

import net.sunken.common.packet.PacketHandler;
import net.sunken.common.server.packet.ServerCacheUpdatePacket;

public class ServerCacheUpdater extends PacketHandler<ServerCacheUpdatePacket> {

    private final ServerObjectCache serverObjectCache;

    public ServerCacheUpdater(ServerObjectCache serverObjectCache) {
        this.serverObjectCache = serverObjectCache;
    }

    @Override
    public void onReceive(ServerCacheUpdatePacket packet) {
        serverObjectCache.updateCache();
    }

}
