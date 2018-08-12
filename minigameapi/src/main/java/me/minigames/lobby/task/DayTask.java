package me.minigames.lobby.task;

import me.minigames.MinigamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class DayTask extends BukkitRunnable {
    public static final long RUN_EVERY = 30 * 20;

    public void run() {
        for (World world : Bukkit.getWorlds()) {
            world.setTime(MinigamePlugin.getInstance().getMinigame().getTime());
        }
    }
}
