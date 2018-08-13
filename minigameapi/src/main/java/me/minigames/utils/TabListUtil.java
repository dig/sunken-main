package me.minigames.utils;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * Created by Digital on 21/01/2018.
 */
public class TabListUtil {
    public static void sendTabTitle(Player player, String header, String footer) {
        if (header == null) header = "";
        header = "\n" + ChatColor.translateAlternateColorCodes('&', header) + "\n";

        if (footer == null) footer = "";
        footer = "\n" + ChatColor.translateAlternateColorCodes('&', footer) + "\n";

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        IChatBaseComponent tabTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent tabFoot = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Field a = headerPacket.getClass().getDeclaredField("a");
            a.setAccessible(true);
            a.set(headerPacket, tabTitle);

            Field b = headerPacket.getClass().getDeclaredField("b");
            b.setAccessible(true);
            b.set(headerPacket, tabFoot);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.sendPacket(headerPacket);
        }
    }
}