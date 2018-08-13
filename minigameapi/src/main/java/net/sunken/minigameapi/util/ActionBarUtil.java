package net.sunken.minigameapi.util;

import net.minecraft.server.v1_13_R1.ChatMessageType;
import net.minecraft.server.v1_13_R1.IChatBaseComponent;
import net.minecraft.server.v1_13_R1.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBarUtil {

    public static void sendActionBarMessage(Player bukkitPlayer, String message) {
        sendRawActionBarMessage(bukkitPlayer, "{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', message) + "\"}");
    }

    public static void sendRawActionBarMessage(Player bukkitPlayer, String rawMessage) {
        CraftPlayer player = (CraftPlayer) bukkitPlayer;
        IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a(rawMessage);
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(chatBaseComponent, ChatMessageType.GAME_INFO);
        player.getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }
}
