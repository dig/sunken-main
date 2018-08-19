package net.sunken.bungeecord.server;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.playerrank.PlayerRankRequired;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.player.PlayerRank;
import net.sunken.common.server.data.ServerObject;
import net.sunken.common.type.ServerType;

import java.net.InetSocketAddress;

public class LobbyCommand {

    @Command(
            aliases = {"lobby", "hub", "lobbies", "main"},
            desc = "Sends player to main lobby.",
            usage = "",
            min = 0,
            max = 0)
    @PlayerRankRequired(PlayerRank.USER)
    public static void lobby(final CommandContext args, final CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            ServerObject lobby = ServerHandler.getFreeServer(ServerType.MAIN_LOBBY);
            if (lobby != null) {
                ServerInfo lobbyObj = ProxyServer.getInstance().constructServerInfo(
                        lobby.getServerName(),
                        new InetSocketAddress(lobby.getServerIp(), lobby.getServerPort()),
                        lobby.getServerName(),
                        false);

                player.connect(lobbyObj);
            }
        }
    }

}
