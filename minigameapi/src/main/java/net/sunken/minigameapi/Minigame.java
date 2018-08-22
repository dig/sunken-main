package net.sunken.minigameapi;

import net.sunken.common.Common;
import net.sunken.common.type.ServerType;
import net.sunken.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Minigame extends MinigameBase {

    public Minigame(JavaPlugin plugin,
                    MinigameInfo information,
                    Lobby lobby,
                    Arena arena) {
        super(plugin, information, lobby, arena);
    }

    public void onMinigameLoad() {
        Common.getInstance().onCommonLoad(
                false,
                ServerType.valueOf(this.getPlugin().getConfig().getString("type")),
                Bukkit.getMaxPlayers(),
                Bukkit.getPort());
        Core.getInstance().onCoreLoad(this.getPlugin());

        this.initialize();
    }

    public void onMinigameDisable() {
        Common.getInstance().onCommonDisable();
        Core.getInstance().onCoreDisable();
    }
}
