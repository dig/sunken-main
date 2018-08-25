package net.sunken.core.model.listener;

import net.sunken.common.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class ModelListener implements Listener {

    private static Map<UUID, Entity> boats;

    static {
        boats = new HashMap<>();
    }

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
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Location location = event.getTo();

        if (location != null) {
            double maxX = location.getX() + 0.5;
            double maxZ = location.getZ() + 0.5;

            double minX = location.getX() - 0.5;
            double minZ = location.getZ() - 0.5;

            boolean onModel = false;
            for (Entity entity : location.getWorld().getEntities()) {
                Location loc = entity.getLocation();

                if (entity.getType().equals(EntityType.ARMOR_STAND)
                        && entity.hasMetadata("Model") && entity.hasMetadata("Walkable")) {

                    if (loc.getX() <= maxX && loc.getX() >= minX) {
                        if (loc.getZ() <= maxZ && loc.getZ() >= minZ) {
                            ArmorStand armorStand = (ArmorStand) entity;

                            entity.setGlowing(true);
                            onModel = true;

                            double height = 1.15;
                            if (armorStand.isSmall()) {
                                height = 0.53;
                            }

                            Location set = entity.getLocation().clone().add(0, height, 0);
                            set.setYaw(player.getLocation().getYaw());

                            Entity boat = null;
                            double oldY = 0;

                            if (boats.containsKey(uuid)) {
                                boat = boats.get(uuid);
                                oldY = boat.getLocation().getY();

                                boat.teleport(set);
                            } else {
                                Boat newBoat = (Boat) location.getWorld().spawnEntity(set, EntityType.BOAT);
                                newBoat.setGravity(false);
                                newBoat.setSilent(true);
                                newBoat.setInvulnerable(true);
                                newBoat.setCustomNameVisible(false);
                                ((CraftEntity) newBoat).getHandle().noclip = true;

                                boat = (Entity) newBoat;
                                boats.put(uuid, boat);
                            }

                            if (boat != null
                                    && set.getY() != oldY) {
                                player.sendMessage("vector");
                                player.setVelocity(player.getVelocity().setY(0.25));
                            }

                            break;
                        }
                    }

                    entity.setGlowing(false);
                }
            }

            // Not on model, remove boat.
            if (!onModel && boats.containsKey(uuid)) {
                boats.get(uuid).remove();
                boats.remove(uuid);
            }
        }
    }

}
