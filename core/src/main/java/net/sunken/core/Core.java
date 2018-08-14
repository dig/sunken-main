package net.sunken.core;

import lombok.Getter;
import net.sunken.core.npc.NPCListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Core {

    @Getter
    private static Core instance = new Core();

    @Getter
    private static Plugin plugin;

    public void onCoreLoad(Plugin plugin, PluginManager pm) {
        this.plugin = plugin;
        this.registerEvents(pm);
    }

    public void onCoreDisable() {

    }

    private void registerEvents(PluginManager pm) {
        pm.registerEvents(new NPCListener(), plugin);
    }

    private Core() {
    }
}
