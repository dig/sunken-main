package net.sunken.core.util;

import com.google.common.collect.Sets;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftBoat;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Boat;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Field;

public class EntityUtil {

    private static Field bField;
    private static Field cField;

    static {
        try {
            bField = ReflectionHelper.getNMSClass("PathfinderGoalSelector").getDeclaredField("b");
            bField.setAccessible(true);

            cField = ReflectionHelper.getNMSClass("PathfinderGoalSelector").getDeclaredField("c");
            cField.setAccessible(true);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setLocation(LivingEntity entity, Location location) {
        EntityLiving ent = ((CraftLivingEntity) entity).getHandle();
        ent.locX = location.getX();
        ent.locY = location.getY();
        ent.locZ = location.getZ();
        ent.yaw = location.getYaw();
        ent.pitch = location.getPitch();
    }

    public static void setLocation(Boat entity, Location location) {
        EntityBoat ent = ((CraftBoat) entity).getHandle();
        ent.locX = location.getX();
        ent.locY = location.getY();
        ent.locZ = location.getZ();
        ent.yaw = location.getYaw();
        ent.pitch = location.getPitch();

        ent.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public static void clearPathFinding(LivingEntity entity) {
        EntityInsentient entityInsentient = (EntityInsentient) ((CraftLivingEntity) entity).getHandle();

        try {
            bField.set(entityInsentient.goalSelector, Sets.newLinkedHashSet());
            bField.set(entityInsentient.targetSelector, Sets.newLinkedHashSet());
            cField.set(entityInsentient.goalSelector, Sets.newLinkedHashSet());
            cField.set(entityInsentient.targetSelector, Sets.newLinkedHashSet());

            EntityCreature ec = (EntityCreature)((CraftEntity) entity).getHandle();
            entityInsentient.goalSelector.a(2, new PathfinderGoalMeleeAttack(ec, 1.0D, false));
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
     * This method changes the yaw (or rotation) of an entity, works on
     * entities with or without AI. Might not work on an entity with AI
     * whilst it is in the middle of pathfinding.
     * @param e Target entity.
     * @param yaw Target yaw.
     */

    public static void setYaw(org.bukkit.entity.Entity e, float yaw) {
        try {
            Location loc = e.getLocation();
            loc.setYaw(yaw);
            Object craftEntity = ReflectionHelper.getBukkitClass("entity.CraftEntity").cast(e);
            Object nmsEntity = craftEntity.getClass().getMethod("getHandle").invoke(craftEntity);
            nmsEntity.getClass().getMethod("setHeadRotation", float.class).invoke(nmsEntity, yaw);
            nmsEntity.getClass().getMethod("k", float.class).invoke(nmsEntity, yaw); //the method after setHeadRotation() and before getAbsportionHearts() in EntityLiving.class, (1.12: h, 1.13: k)
            nmsEntity.getClass().getMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class).invoke(nmsEntity, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } catch(Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * This method changes the pitch of an entity, works on entities
     * with or without AI though it isn't as useful for entities with
     * AI since entity's with AI often move their head. Which often
     * immediately reverts the changes this method makes to the pitch.
     * If you're not standing inside the radius in which they'll look
     * at the player you can see the effects.
     * @param e Target entity.
     * @param pitch Target pitch.
     */

    public static void setPitch(org.bukkit.entity.Entity e, float pitch) {
        try {
            Location loc = e.getLocation();
            loc.setPitch(pitch);
            Object craftEntity = ReflectionHelper.getBukkitClass("entity.CraftEntity").cast(e);
            Object nmsEntity = craftEntity.getClass().getMethod("getHandle").invoke(craftEntity);
            nmsEntity.getClass().getMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class).invoke(nmsEntity, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        } catch(Exception x) {
            x.printStackTrace();
        }
    }

}
