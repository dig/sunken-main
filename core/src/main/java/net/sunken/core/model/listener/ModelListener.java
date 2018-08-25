package net.sunken.core.model.listener;

import net.sunken.core.Core;
import net.sunken.core.util.EntityUtil;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.*;

public class ModelListener implements Listener {

    private static double floorRadius = 1.5;
    private static Map<UUID, List<Boat>> boats;

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

    /* @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Location location = event.getTo();
        Location feet = location.clone().add(0, -1, 0);

        if (location != null) {
            double maxX = location.getX() + 0.5;
            double maxZ = location.getZ() + 0.5;

            double minX = location.getX() - 0.5;
            double minZ = location.getZ() - 0.5;

            boolean onModel = false;
            Entity found = null;
            double distanceToFeet = 0;

            for (Entity entity : location.getWorld().getEntities()) {
                Location loc = entity.getLocation();

                if (entity.getType().equals(EntityType.ARMOR_STAND)
                        && entity.hasMetadata("Model") && entity.hasMetadata("Walkable")) {

                    if (loc.getX() <= maxX && loc.getX() >= minX) {
                        if (loc.getZ() <= maxZ && loc.getZ() >= minZ) {
                            if (found != null) {
                                if (loc.distance(feet) < distanceToFeet) {
                                    found = entity;
                                    distanceToFeet = loc.distance(feet);
                                }
                            } else {
                                found = entity;
                                distanceToFeet = loc.distance(feet);
                            }
                        }
                    }

                    entity.setGlowing(false);
                }
            }

            // Teleport boat on found entity.
            if (found != null) {
                ArmorStand armorStand = (ArmorStand) found;

                found.setGlowing(true);
                onModel = true;

                double height = 1.15;
                if (armorStand.isSmall()) {
                    height = 0.53;
                }

                Location set = found.getLocation().clone().add(0, height, 0);
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
            }

            // Not on model, remove boat.
            if (!onModel && boats.containsKey(uuid)) {
                boats.get(uuid).remove();
                boats.remove(uuid);
            }
        }
    } */

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Location location = event.getTo();

        if (location != null) {
            double maxX = location.getX() + floorRadius;
            double maxY = location.getY() + (floorRadius * 2.5);
            double maxZ = location.getZ() + floorRadius;

            double minX = location.getX() - floorRadius;
            double minY = location.getY() - (floorRadius * 2.5);
            double minZ = location.getZ() - floorRadius;

            long update = System.currentTimeMillis();

            List<Boat> playerBoats = new ArrayList<>();
            if (boats.containsKey(uuid)) {
                playerBoats = boats.get(uuid);
            }

            for (Entity entity : location.getWorld().getEntities()) {
                Location loc = entity.getLocation();

                if (entity.getType().equals(EntityType.ARMOR_STAND)
                        && entity.hasMetadata("Model") && entity.hasMetadata("Walkable")) {
                    ArmorStand armorStand = (ArmorStand) entity;

                    if (loc.getX() <= maxX && loc.getX() >= minX) {
                        if (loc.getZ() <= maxZ && loc.getZ() >= minZ) {
                            if (loc.getY() <= maxY && loc.getY() >= minY) {
                                double height = 1.15;
                                if (armorStand.isSmall()) {
                                    height = 0.25;
                                }

                                Location set = loc.clone().add(0, height, 0);
                                set.setYaw(player.getLocation().getYaw());

                                boolean moved = this.moveBoatTo(player, set, playerBoats, update);

                                if (!moved && playerBoats.size() < 9) {
                                    Boat boat = (Boat) location.getWorld().spawnEntity(set, EntityType.BOAT);
                                    boat.setGravity(false);
                                    boat.setSilent(true);
                                    boat.setInvulnerable(true);
                                    boat.setCustomNameVisible(false);
                                    ((CraftEntity) boat).getHandle().noclip = true;
                                    boat.setMetadata("update", new FixedMetadataValue(Core.getPlugin(), update));

                                    playerBoats.add(boat);
                                }

                                entity.setGlowing(true);
                                continue;
                            }
                        }
                    }

                    entity.setGlowing(false);
                }
            }

            boats.put(uuid, playerBoats);
        }
    }

    private boolean moveBoatTo(Player player, Location location, List<Boat> boats, long update) {
        for (Boat boat : boats) {
            if (boat.getMetadata("update").get(0).asLong() != update) {
                boat.setMetadata("update", new FixedMetadataValue(Core.getPlugin(), update));
                // boat.teleport(location);
                EntityUtil.setLocation(boat, location);

                return true;
            }
        }

        return false;
    }

}
