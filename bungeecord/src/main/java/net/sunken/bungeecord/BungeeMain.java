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

    @Getter
    private static Common common = Common.getInstance();

    @Override
    public void onEnable(){
        instance = this;

        // Initialize common
        common.onCommonLoad(ServerType.BUNGEECORD);

        // Register events
        this.registerEvents();
    }

    @Override
    public void onDisable(){
        common.onCommonDisable();
    }

    private void registerEvents(){
        PluginManager pm = this.getProxy().getPluginManager();

        pm.registerListener(this, new JoinListener());
        pm.registerListener(this, new ConnectListener());
        pm.registerListener(this, new PingListener());
    }
}
