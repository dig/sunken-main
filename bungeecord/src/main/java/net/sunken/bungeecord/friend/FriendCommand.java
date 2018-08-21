package net.sunken.bungeecord.friend;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.playerrank.PlayerRankRequired;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.packet.PartyInviteSendPacket;
import net.sunken.common.player.PlayerRank;

public class FriendCommand {


    @Command(
            aliases = {"friend"},
            desc = "Base friend command",
            usage = "/friend <player>",
            min = 1,
            max = 1)
    @PlayerRankRequired(PlayerRank.USER)
    public static void friend(final CommandContext args, final CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            String firstArg = args.getString(0);



            PartyInviteSendPacket partyInviteSendPacket = new PartyInviteSendPacket(player.getUniqueId(), firstArg);
            PacketUtil.sendPacket(partyInviteSendPacket);
        }
    }

}
