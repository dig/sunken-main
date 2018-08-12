package net.sunken.bungeecord;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMain extends Plugin {

    @Getter
    private static BungeeMain instance;

    public void onEnable(){
        instance = this;
    }

}
