package net.sunken.bungeecord;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.sunken.bungeecord.listeners.ConnectListener;
import net.sunken.bungeecord.listeners.JoinListener;
import net.sunken.bungeecord.listeners.PingListener;
import net.sunken.common.Common;
import net.sunken.common.type.ServerType;

public class BungeeMain extends Plugin {

    @Getter
    private static BungeeMain instance;


    @Override
    public void onEnable(){
        instance = this;

        // Initialize common
        Common.getInstance().onCommonLoad(ServerType.BUNGEECORD);

        // Register events
        this.registerEvents();

        // Get information about running lobbies
        Common.getInstance().getLobbyInfoCache().updateCache();
    }

    @Override
    public void onDisable(){
        Common.getInstance().onCommonDisable();
    }

    private void registerEvents(){
        PluginManager pm = this.getProxy().getPluginManager();

        pm.registerListener(this, new JoinListener());
        pm.registerListener(this, new ConnectListener());
        pm.registerListener(this, new PingListener());
    }
}
