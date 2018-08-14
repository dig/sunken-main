package net.sunken.lobby.listeners;

import net.sunken.common.ServerInstance;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LobbyPlayerCountUpdater implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        ServerInstance.instance().inform(Bukkit.getOnlinePlayers().size());
    }

    @EventHandler(ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) { ServerInstance.instance().inform(Bukkit.getOnlinePlayers().size() - 1);
    }
}
