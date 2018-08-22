package net.sunken.bungeecord.friend;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.playerrank.PlayerRankRequired;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.bungeecord.player.BungeePlayer;
import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.friend.packet.FriendAcceptPacket;
import net.sunken.common.friend.packet.FriendRequestPacket;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.player.PlayerRank;
import org.bson.Document;

public class FriendCommand {

    private static DataManager dataManager;

    static {
        dataManager = Common.getInstance().getDataManager();
    }


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

            if (firstArg.equalsIgnoreCase("list") && args.argsLength() == 1) {
                BungeePlayer bungeePlayer = (BungeePlayer) dataManager.getOnlinePlayers().get(player.getUniqueId());
                bungeePlayer.loadFriendsCache();

                for (Document friend : bungeePlayer.getFriends()) {
                    String name = friend.getString(DatabaseConstants.PLAYER_NAME_FIELD);
                    PlayerRank rank = PlayerRank.valueOf(friend.getString(DatabaseConstants.PLAYER_RANK_FIELD));
                    ChatColor rankColour = ChatColor.valueOf(rank.getColour());

                    player.sendMessage(new TextComponent("- " + rankColour + name));
                }
            } else if (firstArg.equalsIgnoreCase("accept") && args.argsLength() == 2) {
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
