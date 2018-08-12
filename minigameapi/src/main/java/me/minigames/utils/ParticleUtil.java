package me.minigames.utils;

import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by Digital on 21/01/2018.
 * <p>
 * Includes methods for creating particles in a circle
 */
public class ParticleUtil {

    public static void sendParticle(EnumParticle particle, Location loc, Integer speed, Integer amount) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true,
                (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0.0F, 0.0F, 0.0F, speed.intValue(), amount.intValue(),
                null);
        for (Player d : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) d).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void sendLocalParticle(Player target, EnumParticle particle, Location loc, Integer speed, Integer amount) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true,
                (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0.0F, 0.0F, 0.0F, speed.intValue(), amount.intValue(),
                null);
        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendParticleAroundPoint(EnumParticle particle, Location center, double radius) {
        for (int i = 0; i <= 360; i++) {
            double radYaw = Math.toRadians(i);
            double cosRad = Math.cos(radYaw);
            double sinRad = Math.sin(radYaw);

            double x = (radius * cosRad) - (radius * sinRad);
            double z = (radius * sinRad) + (radius * cosRad);

            Location part = center.clone().add(x, 0, z);
            sendParticle(particle, part, 0, 1);
        }
    }

    public static void sendParticleAroundPointAndY(EnumParticle particle, Location center, double radius) {
        for (int i = 0; i <= 360; i++) {
            double radYaw = Math.toRadians(i);
            double cosRad = Math.cos(radYaw);
            double sinRad = Math.sin(radYaw);

            double x = (radius * cosRad) - (radius * sinRad);
            double z = (radius * sinRad) + (radius * cosRad);

            Location part = center.clone().add(x, (0.01 * i), z);
            sendParticle(particle, part, 0, 1);
        }
    }

    public static void sendLocalParticleAroundPoint(Player player, EnumParticle particle, Location center, double radius) {
        for (int i = 0; i <= 360; i++) {
            double radYaw = Math.toRadians(i);
            double cosRad = Math.cos(radYaw);
            double sinRad = Math.sin(radYaw);

            double x = (radius * cosRad) - (radius * sinRad);
            double z = (radius * sinRad) + (radius * cosRad);

            Location part = center.clone().add(x, 0, z);
            sendLocalParticle(player, particle, part, 0, 1);
        }
    }

}
