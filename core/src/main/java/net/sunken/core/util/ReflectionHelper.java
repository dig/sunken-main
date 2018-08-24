package net.sunken.core.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflectionHelper {

    private final static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    /**
     * Gets NMS Class (eq. net.minecraft.server.v1.8.R3.ParticleEffect)
     * @param name eq. ParticleEffect
     * @return Class<?>
     */

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets Autlib Class (eq. com.mojang.autlib.GameProfile)
     * @param name eq. GameProfile
     * @return Class<?>
     */

    public static Class<?> getAuthlibClass(String name) {
        try {
            return Class.forName("com.mojang.authlib." + name);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets CraftBukkit Class (eq. org.bukkit.craftbukkit.v1.8.R3.CraftPlayer)
     * @param name eq. CraftPlayer
     * @return Class<?>
     */

    public static Class<?> getBukkitClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Internal use only!
     * @return Bukkit version string, eq. 1.7.R3
     */

    public static String getVersion() {
        return version;
    }

    /**
     * Sends the actual packet to player, not version-dependend
     * @param packet extends Packet
     * @param p PlayerConnection Target
     * @throws Exception Will probably never throw exception, otherwise blame Mojang/md_5!
     */

    public static void sendPacket(Object packet, Player p) throws Exception {
        Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
        Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
        playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
    }

}
