package net.sunken.bungeecord.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.sunken.bungeecord.BungeeMain;
import net.sunken.bungeecord.Constants;
import net.sunken.bungeecord.lobby.LobbyHandler;
import net.sunken.bungeecord.util.MessageUtil;
import net.sunken.common.server.ServerObject;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JoinListener implements Listener {

    private ArrayList<String> joined = new ArrayList<>();
    private static Logger logger = BungeeMain.getInstance().getLogger();

    @EventHandler
    public void onPreJoin(PreLoginEvent event) {
        ServerObject lobby = LobbyHandler.getFreeLobby();
        // No lobbies available, kick
        if (lobby == null) {
            event.setCancelReason(MessageUtil.stringToComponent(Constants.NO_LOBBY));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInitialJoin(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        if (!joined.contains(uuid)) {
            joined.add(uuid);

            ServerObject lobby = LobbyHandler.getFreeLobby();
            if (lobby != null) {
                ServerInfo lobbyObj = ProxyServer.getInstance().constructServerInfo(
                        lobby.getServerName(),
                        new InetSocketAddress(lobby.getServerIp(), lobby.getServerPort()),
                        lobby.getServerName(),
                        false);

                logger.log(Level.INFO, "Connecting " + player.getName() + " to lobby " + lobby.getServerName()
                        + " (" + lobby.getServerIp() + ")");
                event.setTarget(lobbyObj);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        joined.remove(player.getUniqueId().toString());
    }
}
