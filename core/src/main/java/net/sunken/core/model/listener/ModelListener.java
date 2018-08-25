package net.sunken.core.model.listener;

import net.sunken.core.Core;
import net.sunken.core.util.EntityUtil;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.*;

public class ModelListener implements Listener {

    // Preventing the boat from sliding away from the player
    @EventHandler
    public void onBlockBoatSlide(VehicleBlockCollisionEvent event){
        event.getVehicle().setVelocity(new Vector(0, 0, 0));
    }

    @EventHandler
    public void onEntityBoatSlide(VehicleEntityCollisionEvent event){
        event.getVehicle().setVelocity(new Vector(0, 0, 0));
        event.setCancelled(true);
        event.setCollisionCancelled(true);
    }

    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();

        if (entity.hasMetadata("Model")) {
            event.setCancelled(true);
        }
    }

}
