package net.sunken.bungeecord.friend;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.playerrank.PlayerRankRequired;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.friend.packet.FriendAcceptPacket;
import net.sunken.common.friend.packet.FriendRequestPacket;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.player.PlayerRank;

public class FriendCommand {


    @Command(
            aliases = {"friend"},
            desc = "Base friend command",
            usage = "/friend <player>",
            min = 1,
            max = 2)
    @PlayerRankRequired(PlayerRank.USER)
    public static void friend(final CommandContext args, final CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            String firstArg = args.getString(0);

            if (firstArg.equalsIgnoreCase("accept") && args.argsLength() == 2) {
                FriendAcceptPacket friendAcceptPacket = new FriendAcceptPacket(player.getUniqueId(), args.getString(1), false);
                PacketUtil.sendPacket(friendAcceptPacket);
            } else if (firstArg.equalsIgnoreCase("deny") && args.argsLength() == 2) {
                FriendAcceptPacket friendAcceptPacket = new FriendAcceptPacket(player.getUniqueId(), args.getString(1), true);
                PacketUtil.sendPacket(friendAcceptPacket);
            } else {
                FriendRequestPacket friendRequestPacket = new FriendRequestPacket(player.getUniqueId(), firstArg);
                PacketUtil.sendPacket(friendRequestPacket);
            }
        }
    }

}
