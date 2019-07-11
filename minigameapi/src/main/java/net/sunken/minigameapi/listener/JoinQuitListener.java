package net.sunken.minigameapi.listener;

import net.sunken.minigameapi.ArenaState;
import net.sunken.minigameapi.MinigameBase;
import net.sunken.minigameapi.MinigameInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinQuitListener implements Listener {

    private final MinigameBase minigame;

    public JoinQuitListener(MinigameBase minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        ArenaState arenaState = minigame.getArena().getState();
        if (arenaState != ArenaState.LOBBY) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "The minigame is already in progress.");
            return;
        }

        MinigameInfo mgInfo = minigame.getInformation();

        int currentPlayers = Bukkit.getOnlinePlayers().size();
        int maxPlayers = mgInfo.getMaxPlayers();
        if (currentPlayers >= maxPlayers) {
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, "The minigame is full.");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        ArenaState arenaState = minigame.getArena().getState();
        if (arenaState == ArenaState.LOBBY) {
            player.teleport(minigame.getLobby().getSpawnLocation());
        }
    }
}
