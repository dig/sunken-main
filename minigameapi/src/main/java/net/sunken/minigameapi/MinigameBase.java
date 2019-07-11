package net.sunken.minigameapi;

import lombok.Getter;
import net.sunken.minigameapi.listener.ArenaStateChangeListener;
import net.sunken.minigameapi.listener.JoinQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MinigameBase {

    @Getter
    private final JavaPlugin plugin;
    @Getter
    private MinigameInfo information;
    @Getter
    private final Lobby lobby;
    @Getter
    private final Arena arena;

    MinigameBase(JavaPlugin plugin,
                 MinigameInfo information,
                 Lobby lobby,
                 Arena arena) {
        this.plugin = plugin;
        this.information = information;
        this.lobby = lobby;
        this.arena = arena;
    }

    void initialize() {
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(this), plugin);
        Bukkit.getPluginManager().registerEvents(new ArenaStateChangeListener(), plugin);

        this.arena.setState(ArenaState.LOBBY);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new LobbyPoller(this), 20, 20);
    }
}
