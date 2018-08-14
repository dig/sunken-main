package net.sunken.core.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BungeeUtil {

    public static void sendPlayerToLobby(Player player, Plugin instance){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("lobby");
        out.writeUTF(player.getName());
        player.sendPluginMessage(instance, "BungeeCord", out.toByteArray());
    }

    public static void sendPlayerToServer(Player player, String server, Plugin instance){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("connect");
        out.writeUTF(server);
        player.sendPluginMessage(instance, "BungeeCord", out.toByteArray());
    }

}
