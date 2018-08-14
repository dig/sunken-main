package net.sunken.minigameapi;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.core.Core;
import org.bukkit.plugin.Plugin;

public class Minigame {

    @Getter
    private static Plugin instance;

    public void onMinigameLoad(Plugin plugin) {
        instance = plugin;

        // Load the utils
        Common.getInstance().onCommonLoad(false);
        Core.getInstance().onCoreLoad(plugin);
    }

    public void onMinigameDisable() {
        Core.getInstance().onCoreDisable();
        Common.getInstance().onCommonDisable();
    }

}
