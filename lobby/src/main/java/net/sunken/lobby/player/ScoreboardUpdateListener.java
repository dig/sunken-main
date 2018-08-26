package net.sunken.lobby.player;

import net.sunken.common.Common;
import net.sunken.common.event.ListensToEvent;
import net.sunken.common.event.SunkenListener;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.server.event.ServerCacheUpdateEvent;

public class ScoreboardUpdateListener implements SunkenListener {

    @ListensToEvent
    public void onServerCacheUpdate(ServerCacheUpdateEvent event) {
        for (AbstractPlayer player : Common.getInstance().getDataManager().getOnlinePlayers().values()) {
            LobbyPlayer lobbyPlayer = (LobbyPlayer) player;
            lobbyPlayer.updateScoreboard();
        }
    }
}
