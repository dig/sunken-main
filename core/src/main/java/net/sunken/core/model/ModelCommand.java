package net.sunken.core.model;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.playerrank.PlayerRankRequired;
import net.sunken.common.player.PlayerRank;
import net.sunken.core.Core;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class ModelCommand {

    @Command(
            aliases = {"model"},
            desc = "Spawn a custom model.",
            usage = "/model <name>",
            min = 1,
            max = 1)
    @PlayerRankRequired(PlayerRank.DEVELOPER)
    public static void servers(final CommandContext args, final CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            String name = args.getString(0);
            File file = new File(Core.getPlugin().getDataFolder(), "model" + File.separator + name + ".sunken");

            if (file.exists()) {
                ModelContainer container = new ModelContainer(file.getAbsolutePath());
                Model model = new Model(container, player.getLocation());

                player.sendMessage("Model spawned on your location!");
            } else {
                player.sendMessage("No model found.");
            }
        }
    }

}
