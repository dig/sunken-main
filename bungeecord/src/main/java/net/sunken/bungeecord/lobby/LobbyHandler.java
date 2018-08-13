package net.sunken.bungeecord.lobby;

import net.sunken.bungeecord.BungeeMain;
import net.sunken.common.lobby.LobbyInfo;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class LobbyHandler {

    public static LobbyInfo getFreeLobby(){
        Set<LobbyInfo> lobbies = BungeeMain.getCommon().getLobbyInfoCache().getCache();

        lobbies.stream()
                .sorted(Comparator.comparing(LobbyInfo::getPlayerCount))
                .collect(Collectors.toList());

        int mid = Math.round(lobbies.size() / 2);

    }

}
