package net.sunken.bungeecord.friend;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.NestedCommand;
import com.sk89q.minecraft.util.commands.playerrank.PlayerRankRequired;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.bungeecord.player.BungeePlayer;
import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.friend.packet.MFriendAcceptPacket;
import net.sunken.common.friend.packet.MFriendRemovePacket;
import net.sunken.common.friend.packet.MFriendRequestPacket;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.player.PlayerRank;
import org.bson.Document;

public class FriendCommands {

    private static DataManager dataManager;

    static {
        dataManager = Common.getInstance().getDataManager();
    }

    @Command(
            aliases = {"add", "invite"},
            desc = "Add a player to your friends list.",
            usage = "<player>",
            min = 1,
            max = 1)
    @PlayerRankRequired(PlayerRank.USER)
    public static void add(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        String args0 = args.getString(0);
        PacketUtil.sendPacket(new MFriendRequestPacket(player.getUniqueId(), args0));
    }

    @Command(
            aliases = {"accept"},
            desc = "Accept a friend request.",
            usage = "<player>",
            min = 1,
            max = 1)
    @PlayerRankRequired(PlayerRank.USER)
    public static void accept(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        String args0 = args.getString(0);
        PacketUtil.sendPacket(new MFriendAcceptPacket(player.getUniqueId(), args0, false));
    }

    @Command(
            aliases = {"deny"},
            desc = "Deny a friend request.",
            usage = "<player>",
            min = 1,
            max = 1)
    @PlayerRankRequired(PlayerRank.USER)
    public static void deny(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        String args0 = args.getString(0);
        PacketUtil.sendPacket(new MFriendAcceptPacket(player.getUniqueId(), args0, true));
    }

    @Command(
            aliases = {"list"},
            desc = "Displays all friends.",
            usage = "",
            min = 0,
            max = 0)
    @PlayerRankRequired(PlayerRank.USER)
    public static void list(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;

        BungeePlayer bungeePlayer = (BungeePlayer) dataManager.getOnlinePlayers().get(player.getUniqueId());
        bungeePlayer.loadFriendsCache();

        for (Document friend : bungeePlayer.getFriends()) {
            String name = friend.getString(DatabaseConstants.PLAYER_NAME_FIELD);
            PlayerRank rank = PlayerRank.valueOf(friend.getString(DatabaseConstants.PLAYER_RANK_FIELD));
            ChatColor rankColour = ChatColor.valueOf(rank.getColour());

            player.sendMessage(new TextComponent(rankColour + name));
        }
    }

    @Command(
            aliases = {"remove"},
            desc = "Remove a friend.",
            usage = "<player>",
            min = 1,
            max = 1)
    @PlayerRankRequired(PlayerRank.USER)
    public static void remove(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        String args0 = args.getString(0);
        PacketUtil.sendPacket(new MFriendRemovePacket(player.getUniqueId(), args0));
    }

    public static class Parent {

        @Command(
                aliases = {"friend"},
                desc = "Base friend command")
        @NestedCommand(value = FriendCommands.class, executeBody = true)
        public static void parent(final CommandContext args, final CommandSender sender) {
            sender.sendMessage(new TextComponent("Friend help"));
            sender.sendMessage(new TextComponent("/friend"));
        }
    }

}
