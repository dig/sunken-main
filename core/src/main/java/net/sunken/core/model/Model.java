package net.sunken.core.model;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.core.Core;
import net.sunken.core.model.type.*;
import net.sunken.core.util.EntityUtil;
import net.sunken.core.util.SkullUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftLivingEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;

import javax.swing.text.html.ListView;
import javax.swing.text.html.parser.Entity;
import java.util.*;
import java.util.logging.Level;

public class Model {

    @Getter
    private ModelContainer container;
    @Getter
    private Location location;
    @Getter
    private boolean spawned;

    @Getter
    private Map<Structure, LivingEntity> entities;

    public Model (ModelContainer container, Location location) {
        if (!container.isLoaded()) {
            return;
        }

        this.container = container;
        this.location = location;
        this.spawned = false;
        this.entities = new HashMap<>();

        this.spawn();
    }

    private void spawn (){
        if (!this.spawned) {
            this.spawned = true;

            for (Structure structure : this.container.getStructures()) {
                LivingEntity entity = null;

                if (structure.getSize() == StructureSize.LARGE
                        || structure.getSize() == StructureSize.MEDIUM) {
                    ArmorStand ent = this.spawnArmorstand(structure);
                    entity = (LivingEntity) ent;
                } else {
                    Villager villager = this.spawnVillager(structure);
                    entity = (Villager) villager;
                }

                this.entities.put(structure, entity);

                Material material = null;
                String matRaw = structure.getMaterial();

                if (container.getConversion().containsKey(matRaw)) {
                    material = Material.valueOf(container.getConversion().get(matRaw).toUpperCase());
                } else {
                    material = Material.valueOf(matRaw);
                }

                ItemStack head = new ItemStack(material, 1);
                if (material.equals(Material.PLAYER_HEAD) && structure.getHead() != null) {
                    Head opt = structure.getHead();
                    head = SkullUtil.addTexture(head, opt.getId(), opt.getTexture());
                }

                // Make walkable if the item is a block.
                if (material.isBlock()) {
                    entity.setMetadata("Walkable", new FixedMetadataValue(Core.getPlugin(), true));
                }

                entity.getEquipment().setHelmet(head);
            }

            this.setRotation(0);
        }
    }

    public void updateEntity(Structure updated) {
        double radYaw = Math.toRadians(this.location.getYaw());
        double cosRad = Math.cos(radYaw);
        double sinRad = Math.sin(radYaw);

        for (Structure structure : this.entities.keySet()) {
            LivingEntity entity = this.entities.get(structure);

            if (structure.getFileName().equals(updated.getFileName())) {
                structure = updated;
                Position position = structure.getPosition();

                double x = (position.getX() * cosRad) - (position.getZ() * sinRad);
                double z = (position.getX() * sinRad) + (position.getZ() * cosRad);

                Location set = this.location.clone().add(x, position.getY(), z);
                set.setPitch(updated.getRotation().getPitch());
                set.setYaw(set.getYaw() + updated.getRotation().getYaw());
                // TODO: Add a global rotation to container

                double offsetY = 0;
                switch (structure.getSize()) {
                    case SMALL:
                        offsetY = -0.065;
                        break;
                    case MEDIUM:
                        offsetY = -0.07;
                        break;
                    case LARGE:
                        offsetY = -0.05;
                        break;
                }

                if (entity instanceof ArmorStand) {
                    ArmorStand armorEnt = (ArmorStand) entity;

                    if (structure.getPose().size() > 0) {
                        for (String type : structure.getPose().keySet()) {
                            Position value = structure.getPose().get(type);
                            EulerAngle angle = new EulerAngle(Math.toRadians(value.getX()),
                                    Math.toRadians(value.getY()), Math.toRadians(value.getZ()));

                            switch (type){
                                case "Head":
                                    armorEnt.setHeadPose(angle);
                                    break;
                                case "Body":
                                    armorEnt.setBodyPose(angle);
                                    break;
                                case "RightArm":
                                    armorEnt.setRightArmPose(angle);
                                    break;
                                case "LeftArm":
                                    armorEnt.setLeftArmPose(angle);
                                    break;
                                case "RightLeg":
                                    armorEnt.setRightLegPose(angle);
                                    break;
                                case "LeftLeg":
                                    armorEnt.setLeftLegPose(angle);
                                    break;
                                default:
                                    assert false : "unknown field";
                            }
                        }
                    }
                }

                set.setY((this.location.getY() - offsetY) + position.getY());

                EntityUtil.setLocation(entity, set);
                EntityUtil.setYaw(entity, set.getYaw());
                EntityUtil.setPitch(entity, set.getPitch());
            }
        }
    }

    public void setRotation(float yaw) {
        if (yaw != this.location.getYaw()) {
            this.location.setYaw(yaw);

            for (Structure structure : this.entities.keySet()) {
                LivingEntity entity = this.entities.get(structure);
                this.updateEntity(structure);
            }
        }
    }

    public boolean playAnimation(String animationName, boolean repeat, boolean reverse) {
        if (this.container.getAnimations().containsKey(animationName)) {
            Common.getLogger().log(Level.INFO, "Starting animation " + animationName);

            Animation animation = this.container.getAnimations().get(animationName);
            AnimationTask task = new AnimationTask(animation, this, repeat, reverse);
            task.start();

            return true;
        }

        return false;
    }

    public void remove() {
        for (LivingEntity entity : this.entities.values()) {
            entity.remove();
        }

        this.entities.clear();
        this.spawned = false;
    }

    private Villager spawnVillager(Structure structure) {
        Location pos = this.location.clone().add(structure.getPosition().getX(),
                structure.getPosition().getY(),
                structure.getPosition().getZ());
        pos.setYaw(pos.getYaw() + structure.getRotation().getYaw());
        pos.setPitch(structure.getRotation().getPitch());

        Villager entity = (Villager) this.location.getWorld().spawnEntity(pos, EntityType.VILLAGER);

        entity.setSilent(true);
        entity.setCustomNameVisible(false);
        entity.setCanPickupItems(false);
        entity.setNoDamageTicks(0);
        entity.setGravity(false);
        entity.setAI(false);
        entity.setInvulnerable(true);
        entity.setCollidable(true);
        entity.setBaby();
        entity.setAgeLock(true);
        entity.setMetadata("Model", new FixedMetadataValue(Core.getPlugin(), true));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false));

        EntityUtil.clearPathFinding(entity);
        ((CraftLivingEntity) entity).getHandle().noclip = true;

        EntityUtil.setYaw(entity, pos.getYaw());
        EntityUtil.setPitch(entity, pos.getPitch());

        return entity;
    }

    private ArmorStand spawnArmorstand(Structure structure) {
        Location pos = this.location.clone().add(structure.getPosition().getX(),
                structure.getPosition().getY(),
                structure.getPosition().getZ());
        pos.setYaw(pos.getYaw() + structure.getRotation().getYaw());
        pos.setPitch(structure.getRotation().getPitch());

        ArmorStand entity = (ArmorStand) this.location.getWorld().spawnEntity(pos, EntityType.ARMOR_STAND);
        entity.setVisible(structure.isVisible());
        entity.setCustomNameVisible(false);
        entity.setBasePlate(false);
        entity.setGravity(false);
        entity.setCustomName(ChatColor.DARK_GRAY + UUID.randomUUID().toString());
        entity.setMetadata("Model", new FixedMetadataValue(Core.getPlugin(), true));

        if (structure.isVisible()) {
            entity.setArms(true);
        }

        if (structure.getPose().size() > 0) {
            for (String type : structure.getPose().keySet()) {
                Position value = structure.getPose().get(type);
                EulerAngle angle = new EulerAngle(Math.toRadians(value.getX()),
                        Math.toRadians(value.getY()), Math.toRadians(value.getZ()));

                switch (type){
                    case "Head":
                        entity.setHeadPose(angle);
                        break;
                    case "Body":
                        entity.setBodyPose(angle);
                        break;
                    case "RightArm":
                        entity.setRightArmPose(angle);
                        break;
                    case "LeftArm":
                        entity.setLeftArmPose(angle);
                        break;
                    case "RightLeg":
                        entity.setRightLegPose(angle);
                        break;
                    case "LeftLeg":
                        entity.setLeftLegPose(angle);
                        break;
                    default:
                        assert false : "unknown field";
                }
            }
        }

        if (structure.getSize() == StructureSize.MEDIUM) {
            entity.setSmall(true);
        } else {
            entity.setSmall(false);
        }

        EntityUtil.setYaw(entity, pos.getYaw());
        EntityUtil.setPitch(entity, pos.getPitch());

        return entity;
    }

}
