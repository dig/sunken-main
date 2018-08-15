package net.sunken.core;

import lombok.Getter;
import net.sunken.core.npc.NPCListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Core {

    @Getter
    private static Core instance = new Core();

    @Getter
    private static Plugin plugin;

    public void onCoreLoad(Plugin plugin) {
        Core.plugin = plugin;
        this.registerEvents();
    }

    public void onCoreDisable() {

    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new NPCListener(), plugin);
    }

    private Core() {
    }
}
