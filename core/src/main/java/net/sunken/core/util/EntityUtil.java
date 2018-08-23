package net.sunken.core.util;

import net.minecraft.server.v1_13_R1.EntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class EntityUtil {

    public static void setLocation(LivingEntity entity, Location location) {
        EntityLiving ent = ((CraftLivingEntity) entity).getHandle();
        ent.locX = location.getX();
        ent.locY = location.getY();
        ent.locZ = location.getZ();
        ent.yaw = location.getYaw();
        ent.pitch = location.getPitch();
    }

}
