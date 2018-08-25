package net.sunken.core.model;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.sunken.common.Common;
import net.sunken.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class WalkTask extends BukkitRunnable {

    private static double floorRadius = 1;
    private static Cache<UUID, Location> locations;

    static {
        locations = CacheBuilder
                .newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public void run() {
        // Add new boats
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = player.getLocation();
            Location oldLoc = locations.getIfPresent(player.getUniqueId());

            if (oldLoc == null || !(oldLoc.equals(player.getLocation()))) {
                Common.getLogger().log(Level.INFO, "Updating player due to new movement");
                locations.put(player.getUniqueId(), player.getLocation());

                double maxX = location.getX() + floorRadius;
                double maxY = location.getY() + (floorRadius * 3.75);
                double maxZ = location.getZ() + floorRadius;

                double minX = location.getX() - floorRadius;
                double minY = location.getY() - (floorRadius * 3.75);
                double minZ = location.getZ() - floorRadius;

                for (Entity entity : location.getWorld().getEntities()) {
                    Location loc = entity.getLocation();

                    if (entity.getType().equals(EntityType.ARMOR_STAND)
                            && entity.hasMetadata("Model") && entity.hasMetadata("Walkable") && !entity.hasMetadata("WalkBoat")) {
                        ArmorStand armorStand = (ArmorStand) entity;

                        if (loc.getX() <= maxX && loc.getX() >= minX) {
                            if (loc.getZ() <= maxZ && loc.getZ() >= minZ) {
                                if (loc.getY() <= maxY && loc.getY() >= minY) {

                                    double height = 1.15;
                                    if (armorStand.isSmall()) {
                                        height = 0.25;
                                    }

                                    Location set = loc.clone().add(0, height, 0);

                                    Boat boat = (Boat) location.getWorld().spawnEntity(set, EntityType.BOAT);
                                    boat.setGravity(false);
                                    boat.setSilent(true);
                                    boat.setInvulnerable(true);
                                    boat.setCustomNameVisible(false);
                                    ((CraftEntity) boat).getHandle().noclip = true;

                                    entity.setMetadata("WalkBoat", new FixedMetadataValue(Core.getPlugin(), boat.getEntityId()));
                                }
                            }
                        }
                    }
                }


            }
        }

        // Remove old boats
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                Location location = entity.getLocation();

                if (entity.getType().equals(EntityType.ARMOR_STAND)
                        && entity.hasMetadata("Model") && entity.hasMetadata("Walkable") && entity.hasMetadata("WalkBoat")) {
                    ArmorStand armorStand = (ArmorStand) entity;
                    boolean hasPlayer = false;

                    double maxX = location.getX() + floorRadius;
                    double maxY = location.getY() + (floorRadius * 3.75);
                    double maxZ = location.getZ() + floorRadius;

                    double minX = location.getX() - floorRadius;
                    double minY = location.getY() - (floorRadius * 3.75);
                    double minZ = location.getZ() - floorRadius;

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Location loc = player.getLocation();

                        if (player.getWorld().getName().equals(world.getName())) {
                            if (loc.getX() <= maxX && loc.getX() >= minX) {
                                if (loc.getZ() <= maxZ && loc.getZ() >= minZ) {
                                    if (loc.getY() <= maxY && loc.getY() >= minY) {
                                        hasPlayer = true;
                                    }
                                }
                            }
                        }
                    }

                    if (!hasPlayer) {
                        int boatId = entity.getMetadata("WalkBoat").get(0).asInt();

                        for (Entity ent : world.getEntities()) {
                            if (ent.getEntityId() == boatId) {
                                ent.remove();
                            }
                        }

                        entity.removeMetadata("WalkBoat", Core.getPlugin());
                    }
                }
            }
        }
    }

}
