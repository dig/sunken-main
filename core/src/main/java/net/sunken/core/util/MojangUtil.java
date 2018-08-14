package net.sunken.core.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MojangUtil {

    public static GameProfile makeProfile(String name, UUID skinId) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), ChatColor.translateAlternateColorCodes('&', name));
        if (skinId != null && Bukkit.getPlayer(skinId) != null) {
            String[] a = getSkinData(Bukkit.getPlayer(skinId));
            profile.getProperties().put("textures", new Property("textures", a[0], a[1]));
        }
        return profile;
    }

    public static String[] getSkinData(Player player) {
        EntityPlayer playerNMS = ((CraftPlayer) player).getHandle();
        GameProfile profile = playerNMS.getProfile();
        Property property = profile.getProperties().get("textures").iterator().next();
        String texture = property.getValue();
        String signature = property.getSignature();
        return new String[] {texture, signature};
    }

}
