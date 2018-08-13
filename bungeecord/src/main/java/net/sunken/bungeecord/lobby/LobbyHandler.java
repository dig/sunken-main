package net.sunken.bungeecord.lobby;

import net.sunken.bungeecord.BungeeMain;
import net.sunken.common.lobby.LobbyInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LobbyHandler {

    public static LobbyInfo getFreeLobby() {
        Set<LobbyInfo> lobbies = BungeeMain.getCommon().getLobbyInfoCache().getCache();
        List<LobbyInfo> sortedViaPlayerCount = lobbies.stream()
                .sorted(Comparator.comparing(LobbyInfo::getPlayerCount))
                .collect(Collectors.toList());

        double middle = ((double) lobbies.size()) / 2;
        LobbyInfo middleLobby;

        try {
            middleLobby = sortedViaPlayerCount.get((int) middle);
        } catch (IndexOutOfBoundsException e) {
            if (sortedViaPlayerCount.isEmpty()) {
                // no lobby available
            } else {
                middleLobby = sortedViaPlayerCount.get(0); // just get the first
            }
        }
    }
}
