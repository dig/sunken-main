package net.sunken.bungeecord.party;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.NestedCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.packet.PartyInviteSendPacket;

public class PartyCommands {

    @Command(
            aliases = {"invite"},
            desc = "Invite a player to the party",
            usage = "<player> - The player to invite",
            min = 1,
            max = 1)
    public static void invite(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        String args0 = args.getString(0);
        PacketUtil.sendPacket(new PartyInviteSendPacket(player.getUniqueId(), args0));
    }

    @Command(
            aliases = {"join"},
            desc = "Accept a player's party request. If there is no invite, create one",
            usage = "<player> - The player to join",
            min = 1,
            max = 1)
    public static void join(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        String args0 = args.getString(0);
        PacketUtil.sendPacket(new PartyInviteSendPacket(player.getUniqueId(), args0));
    }

    public static class Parent {

        @Command(
                aliases = {"party"},
                desc = "Base party command")
        @NestedCommand(PartyCommands.class)
        public static void parent(final CommandContext args, final CommandSender sender) {
        }
    }
}
