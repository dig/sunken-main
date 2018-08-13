package net.sunken.lobby;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.lobby.LobbyInfo;
import net.sunken.common.type.ServerType;
import net.sunken.lobby.listeners.JoinListener;
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

        this.registerEvents();
        this.inform(0);
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

    public void inform(int count) {
        LobbyInfo lobbyInfo = LobbyInstance.instance().getLobbyInfo();
        LobbyInfo updatedLobbyInfo = lobbyInfo.setPlayerCount(count);

        this.getLogger().log(Level.INFO, "Informing of lobby player count change");
        Common.getInstance().getLobbyChangeInformer().inform(updatedLobbyInfo);
    }
}
