package net.sunken.lobby;

import net.sunken.common.Common;
import net.sunken.common.type.ServerType;
import net.sunken.lobby.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        Common.getInstance().onCommonLoad(ServerType.valueOf(this.getConfig().getString("type")));

        LobbyInstance.instance(); // instantiate the lobby instance on enable

        Bukkit.getPluginManager().registerEvents(new JoinListener(Common.getInstance().getLobbyChangeInformer()), this);
    }
}
