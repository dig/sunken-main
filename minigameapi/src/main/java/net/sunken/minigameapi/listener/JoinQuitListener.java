package net.sunken.minigameapi;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinQuitListener implements Listener {

    private final MinigameInformation mgInformation;

    public JoinQuitListener(MinigameInformation mgInformation) {
        this.mgInformation = mgInformation;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        int currentPlayers = Bukkit.getOnlinePlayers().size();
        int maxPlayers = mgInformation.getMaxPlayers();
        if (currentPlayers >= maxPlayers) {
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, "The minigame is full.");
        }
    }
}
