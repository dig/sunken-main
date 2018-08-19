package com.sk89q.bukkit.util;

import com.sk89q.minecraft.util.commands.CommandsManager;
import net.sunken.common.Common;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.player.PlayerRank;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class BukkitCommandsManager extends CommandsManager<CommandSender> {

    private static Map<String, AbstractPlayer> onlinePlayers;

    static {
        onlinePlayers = Common.getInstance().getOnlinePlayers();
    }

    @Override
    public boolean hasRank(CommandSender sender, PlayerRank rank) {
        if (sender instanceof Player) {
            AbstractPlayer player = onlinePlayers.get(((Player) sender).getUniqueId().toString());
            return player.getRank().ordinal() >= rank.ordinal();
        } else return sender instanceof ConsoleCommandSender;
    }

    @Override
    public boolean hasPermission(CommandSender sender, String perm) {
        return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
    }
}
