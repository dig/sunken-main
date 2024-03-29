package net.sunken.core.model;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.sunken.core.Core;
import net.sunken.core.model.type.StructureSize;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WalkTask extends BukkitRunnable {

    private static double floorRadius = 1.25;
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
                            && entity.hasMetadata("Model") && entity.hasMetadata("Walkable")
                            && !entity.hasMetadata("WalkBoat")) {
                        ArmorStand armorStand = (ArmorStand) entity;
                        StructureSize size = StructureSize.valueOf(entity.getMetadata("Model").get(0).asString());

                        if (loc.getX() <= maxX && loc.getX() >= minX) {
                            if (loc.getZ() <= maxZ && loc.getZ() >= minZ) {
                                if (loc.getY() <= maxY && loc.getY() >= minY) {

                                    double height = -0.5;
                                    if (armorStand.getHelmet() != null && Model.isSlab(armorStand.getHelmet().getType())) {
                                        height = -0.81;
                                    }

                                    Location set = loc.clone().add(0, height, 0);

                                    Shulker shulker = (Shulker) location.getWorld().spawnEntity(set, EntityType.SHULKER);
                                    shulker.setGravity(false);
                                    shulker.setAI(false);
                                    shulker.setSilent(true);
                                    shulker.setInvulnerable(true);
                                    shulker.setColor(DyeColor.WHITE);
                                    shulker.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false));

                                    if (size == StructureSize.SOLID) {
                                        entity.addPassenger(shulker);
                                        entity.setMetadata("WalkBoat", new FixedMetadataValue(Core.getPlugin(), shulker.getEntityId()));
                                    } else {
                                        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(set, EntityType.ARMOR_STAND);
                                        stand.setVisible(false);
                                        stand.setCustomNameVisible(false);
                                        stand.setBasePlate(false);
                                        stand.setGravity(false);
                                        stand.setSmall(((ArmorStand) entity).isSmall());
                                        stand.setMetadata("Model", new FixedMetadataValue(Core.getPlugin(), true));

                                        stand.addPassenger(shulker);
                                        entity.setMetadata("WalkBoat", new FixedMetadataValue(Core.getPlugin(), stand.getEntityId()));
                                    }
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
                    StructureSize size = StructureSize.valueOf(entity.getMetadata("Model").get(0).asString());

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

                        // Remove shulker if passenger of main armorstand
                        if (entity.getPassengers() != null && entity.getPassengers().size() > 0) {
                            for (Entity ent : entity.getPassengers()) {
                                if (ent.getEntityId() == entity.getMetadata("WalkBoat").get(0).asInt()) {
                                    ent.remove();
                                }
                            }
                        } else {
                            for (Entity ent : world.getEntities()) {
                                if (ent.getEntityId() == entity.getMetadata("WalkBoat").get(0).asInt()) {

                                    // Remove shulker
                                    if (ent.getPassengers() != null && ent.getPassengers().size() > 0) {
                                        for (Entity passenger : ent.getPassengers()) {
                                            passenger.remove();
                                        }
                                    }


                                    ent.remove();
                                }
                            }
                        }

                        entity.removeMetadata("WalkBoat", Core.getPlugin());
                    }
                }
            }
        }
    }

}
