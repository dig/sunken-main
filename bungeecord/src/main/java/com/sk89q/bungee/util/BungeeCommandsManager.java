package com.sk89q.bungee.util;

import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.cooldowns.ActiveCooldown;
import com.sk89q.minecraft.util.commands.cooldowns.CooldownManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;
import net.sunken.common.Common;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.player.PlayerRank;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class BungeeCommandsManager extends CommandsManager<CommandSender> {

    private static Map<UUID, AbstractPlayer> onlinePlayers;

    static {
        onlinePlayers = Common.getInstance().getDataManager().getOnlinePlayers();
    }

    @Nullable
    @Override
    public Collection<ActiveCooldown> getCooldowns(CommandSender player) {
        if (player instanceof ProxiedPlayer) {
            UUID uniqueId = ((ProxiedPlayer) player).getUniqueId();
            return CooldownManager.getPlayerCooldowns().get(uniqueId);
        }
        return null;
    }

    @Override
    public void addCooldown(CommandSender player, ActiveCooldown cooldown) {
        if (player instanceof ProxiedPlayer) {
            UUID uniqueId = ((ProxiedPlayer) player).getUniqueId();
            CooldownManager.getPlayerCooldowns().put(uniqueId, cooldown);
        }
    }

    @Override
    public boolean hasRank(CommandSender sender, PlayerRank rank) {
        if (sender instanceof ProxiedPlayer) {
            AbstractPlayer player = onlinePlayers.get(((ProxiedPlayer) sender).getUniqueId());
            return player.getRank().has(rank);
        } else return sender instanceof ConsoleCommandSender;
    }

    @Override
    public boolean hasPermission(CommandSender sender, String perm) {
        return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
    }
}
