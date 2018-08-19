package net.sunken.bungeecord.server;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.sunken.common.Common;
import net.sunken.common.server.data.ServerObject;
import net.sunken.common.type.ServerType;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerHandler {

    @Nullable
    public static ServerObject getFreeServer(ServerType server) {
        List<ServerObject> lobbies = Common.getInstance().getServerCache().getCache(server);

        List<ServerObject> sortedViaPlayerCount = lobbies.stream()
                .sorted(Comparator.comparing(ServerObject::getPlayerCount))
                .collect(Collectors.toList());

        Collections.reverse(sortedViaPlayerCount);

        double middle = ((double) lobbies.size()) / 2;
        int middleFloor = (int) middle;
        ServerObject middleLobby = null;

        for (int i = 0; i < sortedViaPlayerCount.size(); i++) {
            if (middleLobby == null && i >= middleFloor) {
                ServerObject cur = sortedViaPlayerCount.get(i);

                if (cur.getPlayerCount() < cur.getMaxPlayers()) {
                    middleLobby = cur;
                }
            }
        }

        return middleLobby;
    }

    @Nullable
    public static ServerObject getServer(String name){
        Set<ServerObject> servers = Common.getInstance().getServerCache().getCache();

        List<ServerObject> serversList = servers.stream()
                .sorted(Comparator.comparing(ServerObject::getPlayerCount))
                .collect(Collectors.toList());

        for(int i = 0; i < serversList.size(); i++){
            ServerObject serv = serversList.get(i);

            if(serv.getServerName().equals(name)){
                return serv;
            }
        }

        return null;
    }

}
