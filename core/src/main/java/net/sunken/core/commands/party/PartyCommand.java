package net.sunken.core.commands.party;

import com.google.common.collect.Maps;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.playerrank.PlayerRankRequired;
import lombok.Getter;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.packet.request.MPartyCheckRequestPacket;
import net.sunken.common.parties.packet.request.MPartyListRequestPacket;
import net.sunken.common.player.PlayerRank;
import net.sunken.core.inventory.PageContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PartyCommand {

    @Getter
    private static Map<UUID, PageContainer> containers;

    static {
        containers = Maps.newHashMap();
    }

    @Command(
            aliases = {"party", "parties"},
            desc = "Join, leave, or view party information",
            usage = "",
            min = 0,
            max = 0)
    @PlayerRankRequired(PlayerRank.USER)
    public static void party(final CommandContext args, final CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            PacketUtil.sendPacket(new MPartyCheckRequestPacket(player.getUniqueId()));
        }
    }
}
