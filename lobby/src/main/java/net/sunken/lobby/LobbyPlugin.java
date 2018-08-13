package net.sunken.lobby;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.lobby.LobbyInfo;
import net.sunken.common.type.ServerType;
import net.sunken.lobby.listeners.LobbyPlayerCountUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class LobbyPlugin extends JavaPlugin {

    @Getter
    private static LobbyPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        Common.getInstance().onCommonLoad(ServerType.valueOf(this.getConfig().getString("type")));

        LobbyInstance.instance(); // inform of the initial lobby information

        this.registerEvents();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // mark the lobby as removed on shutdown
            Common.getLogger().log(Level.INFO, "shutdown hook called");

            LobbyInfo lobbyInfo = LobbyInstance.instance().getLobbyInfo();
            Common.getInstance().getLobbyChangeInformer().remove(lobbyInfo)
                    .thenRun(() -> Common.getInstance().onCommonDisable());
        }));
    }

    @Override
    public void onDisable() {
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new LobbyPlayerCountUpdater(), this);
    }
}
