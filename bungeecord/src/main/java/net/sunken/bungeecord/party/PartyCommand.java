package net.sunken.bungeecord.party;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.playerrank.PlayerRankRequired;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.player.PlayerRank;

public class PartyCommand {

    @Command(
            aliases = {"gserver"},
            desc = "Global server teleport",
            usage = "<server>",
            min = 1,
            max = 1)
    @PlayerRankRequired(PlayerRank.ADMIN)
    public void party(final CommandContext args, final CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
        }
    }
}