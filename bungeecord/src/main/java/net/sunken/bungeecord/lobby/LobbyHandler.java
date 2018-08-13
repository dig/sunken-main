package net.sunken.bungeecord.lobby;

import net.sunken.common.Common;
import net.sunken.common.lobby.LobbyInfo;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LobbyHandler {

    @Nullable
    public static LobbyInfo getFreeLobby() {
        Set<LobbyInfo> lobbies = Common.getInstance().getLobbyInfoCache().getCache();
        List<LobbyInfo> sortedViaPlayerCount = lobbies.stream()
                .sorted(Comparator.comparing(LobbyInfo::getPlayerCount))
                .collect(Collectors.toList());

        double middle = ((double) lobbies.size()) / 2;
        int middleFloor = (int) middle;
        LobbyInfo middleLobby = null;

        try {
            middleLobby = sortedViaPlayerCount.get(middleFloor - 1); // indices start at 0
        } catch (IndexOutOfBoundsException e) {
            if (!sortedViaPlayerCount.isEmpty()) {
                middleLobby = sortedViaPlayerCount.get(0); // just get the first
            }
        }

        return middleLobby;
    }
}
