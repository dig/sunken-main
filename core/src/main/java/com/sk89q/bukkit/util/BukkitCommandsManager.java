package com.sk89q.bukkit.util;

import com.sk89q.minecraft.util.commands.CommandsManager;
import net.sunken.common.Common;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.player.PlayerRank;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class BukkitCommandsManager extends CommandsManager<CommandSender> {

    private static Map<UUID, AbstractPlayer> onlinePlayers;

    static {
        onlinePlayers = Common.getInstance().getDataManager().getOnlinePlayers();
    }

    @Override
    public boolean hasRank(CommandSender sender, PlayerRank rank) {
        if (sender instanceof Player) {
            AbstractPlayer player = onlinePlayers.get(((Player) sender).getUniqueId());
            return player.getRank().has(rank);
        } else return sender instanceof ConsoleCommandSender;
    }

    @Override
    public boolean hasPermission(CommandSender sender, String perm) {
        return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
    }
}
