package net.sunken.bungeecord.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.sunken.bungeecord.Constants;
import net.sunken.bungeecord.lobby.LobbyHandler;
import net.sunken.common.lobby.LobbyInfo;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class JoinListener implements Listener {

    private ArrayList<String> joined = new ArrayList<String>();

    @EventHandler
    public void onPreJoin(PreLoginEvent event) {
        LobbyInfo lobby = LobbyHandler.getFreeLobby();

        // No lobbies available, kick
        if(lobby == null){
            event.setCancelReason(ChatColor.translateAlternateColorCodes('&', Constants.NO_LOBBY));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInitialJoin(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if(!joined.contains(player.getUniqueId().toString())){
            joined.add(player.getUniqueId().toString());

            LobbyInfo lobby = LobbyHandler.getFreeLobby();

            if(lobby != null){
                ServerInfo lobbyObj = ProxyServer.getInstance().constructServerInfo(
                        lobby.getServerName(),
                        new InetSocketAddress(lobby.getServerIp(), lobby.getServerPort()),
                        lobby.getServerName(),
                        false);

                System.out.print("Connecting " + player.getName() + " to lobby " + lobby.getServerName() + " (" + lobby.getServerIp() + ")");
                event.setTarget(lobbyObj);
            }
            else{
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if(joined.contains(player.getUniqueId().toString())){
            joined.remove(player.getUniqueId().toString());
        }
    }

}
