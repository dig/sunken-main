package net.sunken.core.server;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.playerrank.PlayerRankRequired;
import lombok.Getter;
import net.sunken.common.player.PlayerRank;
import net.sunken.core.inventory.PageContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ServersCommand {

    @Getter
    private static PageContainer container;

    static {
        container = new PageContainer();
    }

    @Command(
            aliases = {"servers", "server"},
            desc = "Connect to any server on the network.",
            usage = "",
            min = 0,
            max = 0)
    @PlayerRankRequired(PlayerRank.DEVELOPER)
    public static void servers(final CommandContext args, final CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            container.launchFor(player);
        }
    }

}
