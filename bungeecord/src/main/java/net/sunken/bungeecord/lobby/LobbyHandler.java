package net.sunken.bungeecord.lobby;

import net.sunken.common.Common;
import net.sunken.common.lobby.LobbyInfo;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class LobbyHandler {

    @Nullable
    public static LobbyInfo getFreeLobby() {
        Set<LobbyInfo> lobbies = Common.getInstance().getLobbyInfoCache().getCache();
        List<LobbyInfo> sortedViaPlayerCount = lobbies.stream()
                .sorted(Comparator.comparing(LobbyInfo::getPlayerCount))
                .collect(Collectors.toList());

        Collections.reverse(sortedViaPlayerCount);

        double middle = ((double) lobbies.size()) / 2;
        int middleFloor = (int) middle;
        LobbyInfo middleLobby = null;

        for(int i = 0; i<sortedViaPlayerCount.size(); i++){
            if(middleLobby == null && i >= middleFloor){
                LobbyInfo cur = sortedViaPlayerCount.get(i);

                if(cur.getPlayerCount() < cur.getMaxPlayers()){
                    middleLobby = cur;
                }
            }
        }

        return middleLobby;
    }
}
