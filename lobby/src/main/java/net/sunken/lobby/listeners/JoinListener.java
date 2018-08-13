package net.sunken.lobby.listeners;

import net.sunken.common.Common;
import net.sunken.common.lobby.LobbyChangeInformer;
import net.sunken.common.lobby.LobbyInfo;
import net.sunken.lobby.LobbyInstance;
import net.sunken.lobby.LobbyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        LobbyPlugin.getInstance().inform(Bukkit.getOnlinePlayers().size());
    }

    @EventHandler(ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        LobbyPlugin.getInstance().inform(Bukkit.getOnlinePlayers().size() - 1);
    }
}
