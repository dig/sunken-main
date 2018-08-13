package net.sunken.lobby;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.lobby.LobbyInfo;
import net.sunken.common.type.ServerType;
import net.sunken.lobby.listeners.JoinListener;
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
        Common.getInstance().onCommonLoad(ServerType.valueOf(this.getConfig().getString("type")));

        LobbyInstance.instance(); // inform of the initial lobby information

        this.registerEvents();
    }

    @Override
    public void onDisable() {
        LobbyInfo lobbyInfo = LobbyInstance.instance().getLobbyInfo();
        Common.getInstance().getLobbyChangeInformer().remove(lobbyInfo);

        Common.getInstance().onCommonDisable();
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(), this);
    }
}
