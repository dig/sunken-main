package net.sunken.minigameapi;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.type.ServerType;
import net.sunken.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Minigame {

    @Getter
    private static Plugin instance;

    public void onMinigameLoad(Plugin plugin) {
        instance = plugin;

        // Add server to network and load utils
        Common.getInstance().onCommonLoad(
                false,
                true,
                ServerType.valueOf(plugin.getConfig().getString("type")),
                Bukkit.getMaxPlayers(),
                Bukkit.getPort());
        Core.getInstance().onCoreLoad(plugin);
    }

    public void onMinigameDisable() {
        Core.getInstance().onCoreDisable();
    }
}
