package net.sunken.bungeecord;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.sunken.bungeecord.config.ConfigHandler;
import net.sunken.bungeecord.listeners.ConnectListener;
import net.sunken.bungeecord.listeners.FailListener;
import net.sunken.bungeecord.listeners.JoinListener;
import net.sunken.bungeecord.listeners.PingListener;
import net.sunken.common.Common;
import net.sunken.common.type.ServerType;

public class BungeeMain extends Plugin {

    @Getter
    private static BungeeMain instance;

    @Getter
    private static ConfigHandler configHandler;

    @Override
    public void onEnable(){
        instance = this;

        // Config Handler
        configHandler = new ConfigHandler(this, "config.yml");

        // Initialize common
        Common.getInstance().onCommonLoad(true);

        // Register events
        this.registerEvents();

        // Get information about running servers
        Common.getInstance().getServerCache().updateCache();
    }

    private void registerEvents(){
        PluginManager pm = this.getProxy().getPluginManager();

        pm.registerListener(this, new JoinListener());
        pm.registerListener(this, new ConnectListener());
        pm.registerListener(this, new PingListener());
        pm.registerListener(this, new FailListener());
    }
}
