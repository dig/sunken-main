package net.sunken.lobby;

import net.sunken.common.Common;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        LobbyInstance.instance(); // instantiate the lobby instance on enable

        Bukkit.getPluginManager().registerEvents(new JoinListener(Common.getInstance().getLobbyChangeInformer()), this);
    }
}
