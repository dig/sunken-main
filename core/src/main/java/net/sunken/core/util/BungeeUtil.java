package net.sunken.core.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.sunken.common.type.ServerType;
import net.sunken.core.Core;
import org.bukkit.entity.Player;

public class BungeeUtil {

    public static void sendPlayerToType(Player player, ServerType type){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("type");
        out.writeUTF(player.getUniqueId().toString());
        out.writeUTF(type.toString());

        player.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
    }

    public static void sendPlayerToServer(Player player, String name){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("server");
        out.writeUTF(player.getUniqueId().toString());
        out.writeUTF(name);

        player.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
    }

}
