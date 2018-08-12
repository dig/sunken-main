package me.minigames.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.minigames.MinigamePlugin;
import org.bukkit.entity.Player;

/**
 * Created by Digital on 21/01/2018.
 */
public class BungeeUtil {

    public static void sendPlayerToLobby(Player player){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("lobby");
        out.writeUTF(player.getName());
        player.sendPluginMessage(MinigamePlugin.getInstance(), "BungeeCord", out.toByteArray());
    }

    public static void sendPlayerToServer(Player player, String server){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("connect");
        out.writeUTF(server);
        player.sendPluginMessage(MinigamePlugin.getInstance(), "BungeeCord", out.toByteArray());
    }

}
