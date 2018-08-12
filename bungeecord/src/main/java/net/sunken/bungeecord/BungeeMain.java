package net.sunken.bungeecord;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.sunken.common.Common;
import net.sunken.common.type.ServerType;

public class BungeeMain extends Plugin {

    @Getter
    private static BungeeMain instance;

    private static Common common;

    public void onEnable(){
        instance = this;

        // Initialize common
        common = new Common();
        common.onCommonLoad(ServerType.BUNGEECORD);
    }

    public void onDisable(){
        common.onCommonDisable();
    }

}
