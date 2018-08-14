package net.sunken.lobby;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.lobby.LobbyInfo;
import net.sunken.common.type.ServerType;
import net.sunken.core.Core;
import net.sunken.lobby.listeners.LobbyPlayerCountUpdater;
import net.sunken.lobby.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class LobbyPlugin extends JavaPlugin {

    @Getter
    private static LobbyPlugin instance;

    @Getter
    private ServerType type;

    @Override
    public void onEnable() {
        instance = this;
        this.type = ServerType.valueOf(this.getConfig().getString("type"));

        this.saveDefaultConfig();
        Common.getInstance().onCommonLoad(true);
        Core.getInstance().onCoreLoad(this);

        LobbyInstance.instance(); // inform of the initial lobby information

        this.registerEvents();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LobbyInfo lobbyInfo = LobbyInstance.instance().getLobbyInfo();
            Common.getInstance().getLobbyChangeInformer().removeSync(lobbyInfo);

            Common.getInstance().onCommonDisable();
        }));
    }

    @Override
    public void onDisable() {
        Core.getInstance().onCoreDisable();
        Common.getInstance().onCommonDisable();
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new LobbyPlayerCountUpdater(), this);
        pm.registerEvents(new PlayerListener(), this);
    }
}
