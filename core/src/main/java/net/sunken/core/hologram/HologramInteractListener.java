package net.sunken.core.hologram;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class HologramInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked() != null && event.getRightClicked() instanceof ArmorStand){
            if(event.getRightClicked().hasMetadata("Hologram")){
                event.setCancelled(true);
            }
        }
    }

}
