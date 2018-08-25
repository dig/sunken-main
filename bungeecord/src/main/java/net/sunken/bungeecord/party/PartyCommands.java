package net.sunken.bungeecord.party;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.NestedCommand;
import com.sk89q.minecraft.util.commands.playerrank.PlayerRankRequired;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.packet.request.MPartyInviteRequestPacket;
import net.sunken.common.parties.packet.request.MPartyLeaveRequestPacket;
import net.sunken.common.parties.packet.request.MPartyListRequestPacket;
import net.sunken.common.player.PlayerRank;

/** All the party commands */
public class PartyCommands {

    @Command(
            aliases = {"invite"},
            desc = "Invite a player to the party",
            usage = "<player> - The player to invite",
            min = 1,
            max = 1)
    @PlayerRankRequired(PlayerRank.USER)
    public static void invite(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        String args0 = args.getString(0);
        PacketUtil.sendPacket(new MPartyInviteRequestPacket(player.getUniqueId(), args0));
    }

    @Command(
            aliases = {"join"},
            desc = "Accept a player's party request. If there is no invite, create one",
            usage = "<player> - The player to join",
            min = 1,
            max = 1)
    @PlayerRankRequired(PlayerRank.USER)
    public static void join(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        String args0 = args.getString(0);
        PacketUtil.sendPacket(new MPartyInviteRequestPacket(player.getUniqueId(), args0));
    }

    @Command(
            aliases = {"leave"},
            desc = "Leave a party. If you are the leader, it will be disbanded")
    @PlayerRankRequired(PlayerRank.USER)
    public static void leave(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PacketUtil.sendPacket(new MPartyLeaveRequestPacket(player.getUniqueId(), false));
    }

    @Command(
            aliases = {"disband"},
            desc = "Disband a party")
    @PlayerRankRequired(PlayerRank.USER)
    public static void disband(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PacketUtil.sendPacket(new MPartyLeaveRequestPacket(player.getUniqueId(), true));
    }

    @Command(
            aliases = {"list"},
            desc = "List party members")
    @PlayerRankRequired(PlayerRank.USER)
    public static void list(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PacketUtil.sendPacket(new MPartyListRequestPacket(player.getUniqueId()));
    }

    public static class Parent {

        @Command(
                aliases = {"party"},
                desc = "Base party command")
        @NestedCommand(value = PartyCommands.class, executeBody = true)
        public static void parent(final CommandContext args, final CommandSender sender) {
            // Display party help //
        }
    }
}
