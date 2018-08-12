package me.minigames.lobby.task;

import me.minigames.Minigame;
import me.minigames.MinigamePlugin;
import me.minigames.lobby.LobbyManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class StartPreconditionsTask extends BukkitRunnable {
    private Minigame minigame = MinigamePlugin.getInstance().getMinigame();
    public static final long RUN_EVERY = 5 * 20;
    private long startTime = 0;
    private boolean maxPlayersHit = false;

    public void run() {
        int totalPlayers = Bukkit.getOnlinePlayers().size();

        int requiredPlayers = minigame.getRequiredPlayers();
        int maxPlayers = minigame.getMaxPlayers();

        long now = System.currentTimeMillis();

        if (totalPlayers == maxPlayers) {
            // start in min time if we haven't already hit the
            // max amount of players
            if (!maxPlayersHit) {
                startTime = now + minigame.getMinTime();
                maxPlayersHit = true;
            }
        } else if (totalPlayers >= requiredPlayers && startTime == 0) {
            // start in max time
            startTime = now + minigame.getMaxTime();
            maxPlayersHit = false;
        } else if (totalPlayers < requiredPlayers) {
            // If not enough players, set start time to 0
            startTime = 0;
            maxPlayersHit = false;
        }

        // Countdown is up, start game
        if (startTime != 0 && now >= startTime) {
            MinigamePlugin.getInstance().startGame();

            LobbyManager.removeLobbySchematic();
            this.cancel();
        }
    }
}
