package net.sunken.core.model;

import lombok.Getter;
import net.sunken.core.Core;
import net.sunken.core.model.type.Position;
import net.sunken.core.model.type.Structure;
import net.sunken.core.model.type.StructureSize;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.EulerAngle;

import java.util.UUID;

public class Model {

    @Getter
    private ModelContainer container;
    @Getter
    private Location location;
    @Getter
    private boolean spawned;

    public Model (ModelContainer container, Location location) {
        if (!container.isLoaded()) {
            return;
        }

        this.container = container;
        this.location = location;
        this.spawned = false;

        this.spawn();
    }

    private void spawn (){
        if (!this.spawned) {
            this.spawned = true;

            for (Structure structure : this.container.getStructures()) {
                if (structure.getSize() == StructureSize.LARGE
                        || structure.getSize() == StructureSize.MEDIUM) {
                    ArmorStand entity = this.spawnArmorstand(structure);
                } else {
                    // TODO: Handle villager support (StructureSize.SMALL)
                }

                // TODO: Add block to head
            }
        }
    }

    private ArmorStand spawnArmorstand(Structure structure) {
        Location pos = this.location.add(structure.getPosition().getX(),
                structure.getPosition().getY(),
                structure.getPosition().getZ());

        ArmorStand entity = (ArmorStand) this.location.getWorld().spawnEntity(pos, EntityType.ARMOR_STAND);
        entity.setVisible(structure.isVisible());
        entity.setCustomNameVisible(true);
        entity.setBasePlate(true);
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
