package net.sunken.minigameapi;

import lombok.Getter;
import org.bukkit.plugin.Plugin;

public class Minigame {

    @Getter
    private static Plugin instance;

    public void onMinigameLoad(Plugin plugin) {
        instance = plugin;
    }

    public void onMinigameDisable() {
    }

}
