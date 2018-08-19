package com.sk89q.bungee.util;

import com.sk89q.minecraft.util.commands.CommandsManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;
import net.sunken.common.Common;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.player.PlayerRank;

import java.util.Map;

public class BungeeCommandsManager extends CommandsManager<CommandSender> {

    private static Map<String, AbstractPlayer> onlinePlayers;

    static {
        onlinePlayers = Common.getInstance().getOnlinePlayers();
    }

    @Override
    public boolean hasRank(CommandSender sender, PlayerRank rank) {
        if (sender instanceof ProxiedPlayer) {
            AbstractPlayer player = onlinePlayers.get(((ProxiedPlayer) sender).getUniqueId().toString());
            return player.getRank().has(rank);
        } else return sender instanceof ConsoleCommandSender;
    }

    @Override
    public boolean hasPermission(CommandSender sender, String perm) {
        return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
    }
}
