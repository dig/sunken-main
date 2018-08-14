package net.sunken.bungeecord.lobby;

import net.sunken.common.Common;
import net.sunken.common.server.ServerObject;
import net.sunken.common.type.ServerType;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LobbyHandler {

    @Nullable
    public static ServerObject getFreeLobby() {
        Set<ServerObject> lobbies = Common.getInstance().getServerCache().getCache(ServerType.MAIN_LOBBY);

        List<ServerObject> sortedViaPlayerCount = lobbies.stream()
                .sorted(Comparator.comparing(ServerObject::getPlayerCount))
                .collect(Collectors.toList());

        Collections.reverse(sortedViaPlayerCount);

        double middle = ((double) lobbies.size()) / 2;
        int middleFloor = (int) middle;
        ServerObject middleLobby = null;

        for(int i = 0; i<sortedViaPlayerCount.size(); i++){
            if(middleLobby == null && i >= middleFloor){
                ServerObject cur = sortedViaPlayerCount.get(i);

                if(cur.getPlayerCount() < cur.getMaxPlayers()){
                    middleLobby = cur;
                }
            }
        }

        return middleLobby;
    }
}
