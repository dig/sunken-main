package net.sunken.minigameapi;

import lombok.Getter;
import net.sunken.minigameapi.npc.NPCListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Minigame {

    @Getter
    private static Plugin instance;

    public void onMinigameLoad(Plugin plugin){
        instance = plugin;

        this.registerEvents();
    }

    public void onMinigameDisable(){

    }

    private void registerEvents(){
        PluginManager pm = instance.getServer().getPluginManager();
        pm.registerEvents(new NPCListener(), instance);
    }
}
