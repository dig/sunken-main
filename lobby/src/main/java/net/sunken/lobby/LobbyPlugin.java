package net.sunken.lobby;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.type.ServerType;
import net.sunken.core.Core;
import net.sunken.lobby.listeners.LobbyPlayerCountUpdater;
import net.sunken.lobby.listeners.PlayerListener;
import net.sunken.lobby.listeners.WorldListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyPlugin extends JavaPlugin {

    @Getter
    private static LobbyPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        Common.getInstance().onCommonLoad(true,
                ServerType.valueOf(this.getConfig().getString("type")),
                Bukkit.getMaxPlayers(), Bukkit.getPort());

        Core.getInstance().onCoreLoad(this);

        this.registerEvents();
    }

    @Override
    public void onDisable() {
        Core.getInstance().onCoreDisable();
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new LobbyPlayerCountUpdater(), this);
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new WorldListener(), this);
    }
}
