package net.sunken.core.model;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.core.Core;
import net.sunken.core.model.type.Animation;
import net.sunken.core.model.type.Position;
import net.sunken.core.model.type.Structure;
import net.sunken.core.model.type.StructureSize;
import net.sunken.core.util.EntityUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.EulerAngle;

import javax.swing.text.html.ListView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
                    // TODO: Handle villager support (StructureSize.SMALL)
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
                entity.getEquipment().setHelmet(head);
            }
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
                set.setPitch(0);
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

    public boolean playAnimation(String animationName) {
        if (this.container.getAnimations().containsKey(animationName)) {
            Common.getLogger().log(Level.INFO, "Starting animation " + animationName);

            Animation animation = this.container.getAnimations().get(animationName);
            AnimationTask task = new AnimationTask(animation, this);
            task.start();

            return true;
        }

        return false;
    }

    private ArmorStand spawnArmorstand(Structure structure) {
        Location pos = this.location.clone().add(structure.getPosition().getX(),
                structure.getPosition().getY(),
                structure.getPosition().getZ());
        pos.setYaw(0);
        pos.setPitch(0);

        ArmorStand entity = (ArmorStand) this.location.getWorld().spawnEntity(pos, EntityType.ARMOR_STAND);
        entity.setVisible(structure.isVisible());
        entity.setCustomNameVisible(false);
        entity.setBasePlate(false);
        entity.setGravity(false);
        entity.setCustomName(ChatColor.DARK_GRAY + UUID.randomUUID().toString());
        entity.setMetadata("Model", new FixedMetadataValue(Core.getPlugin(), true));

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

        return entity;
    }

}
