package net.sunken.bungeecord.party;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.playerrank.PlayerRankRequired;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.Common;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.service.PartyService;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.player.PlayerRank;

import java.util.Map;
import java.util.UUID;

public class PartyCommand {

    private static Map<UUID, AbstractPlayer> onlinePlayers = Common.getInstance().getDataManager().getOnlinePlayers();
    private static PartyService partyService = Common.getInstance().getPartyService();

    @Command(
            aliases = {"party"},
            desc = "Base party command",
            usage = "/party <player>",
            min = 1,
            max = 1)
    @PlayerRankRequired(PlayerRank.USER)
    public static void party(final CommandContext args, final CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            String firstArg = args.getString(0);

            // TODO: switch to RedisBungee?
            ProxiedPlayer possiblePlayer = ProxyServer.getInstance().getPlayer(firstArg);
            if (possiblePlayer != null) {
                AbstractPlayer inviterAbstractPlayer = onlinePlayers.get(player.getUniqueId().toString());
                AbstractPlayer inviteeAbstractPlayer = onlinePlayers.get(possiblePlayer.getUniqueId().toString());
                PartyPlayer inviter = new PartyPlayer(player.getUniqueId(),
                                                      player.getName(),
                                                      inviterAbstractPlayer.getRank());
                PartyPlayer invitee = new PartyPlayer(possiblePlayer.getUniqueId(),
                                                      possiblePlayer.getName(),
                                                      inviteeAbstractPlayer.getRank());
                partyService.createParty(inviter, invitee);
            }
        }
    }
}
