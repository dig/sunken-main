package net.sunken.lobby;

import net.sunken.common.lobby.LobbyChangeInformer;
import net.sunken.common.lobby.LobbyInfo;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final LobbyChangeInformer lobbyChangeInformer;

    public JoinListener(LobbyChangeInformer lobbyChangeInformer) {
        this.lobbyChangeInformer = lobbyChangeInformer;
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        LobbyInfo lobbyInfo = LobbyInstance.instance().getLobbyInfo();
        LobbyInfo updatedLobbyInfo = lobbyInfo.setPlayerCount(Bukkit.getOnlinePlayers().size());
        this.lobbyChangeInformer.inform(updatedLobbyInfo);
    }
}
