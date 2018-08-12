package net.sunken.common.lobby;

import com.google.common.collect.Sets;

import java.util.Set;

public class ServerInfoCache {

    private static ServerInfoCache instance = new ServerInfoCache();

    private Set<ServerInfo> cache = Sets.newHashSet();

    public Set<ServerInfo> getCache() {
        return this.cache;
    }

    public static ServerInfoCache instance() {
        return instance;
    }

    private ServerInfoCache() {
    }
}
