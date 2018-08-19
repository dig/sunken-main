package net.sunken.bungeecord.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.sunken.bungeecord.BungeeMain;
import net.sunken.bungeecord.Constants;
import net.sunken.bungeecord.player.BungeePlayer;
import net.sunken.bungeecord.server.ServerHandler;
import net.sunken.bungeecord.util.MessageUtil;
import net.sunken.common.Common;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.server.ServerObject;
import net.sunken.common.type.ServerType;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JoinListener implements Listener {

    private ArrayList<String> joined = new ArrayList<>();

    @EventHandler
    public void onPreJoin(PreLoginEvent event) {

        // Check that we support the version of minecraft
        List<Integer> versions = BungeeMain.getConfigHandler().getConfig().getIntList("versions");
        if(!versions.contains(event.getConnection().getVersion())){
            event.setCancelReason(MessageUtil.stringToComponent(Constants.OUTDATED_VER));
            event.setCancelled(true);
        }

        // No lobbies available, kick
        ServerObject lobby = ServerHandler.getFreeServer(ServerType.MAIN_LOBBY);
        if (lobby == null) {
            event.setCancelReason(MessageUtil.stringToComponent(Constants.NO_LOBBY));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onOfficialJoin(PostLoginEvent event){
        ProxiedPlayer player = event.getPlayer();

        // Add to onlinePlayers
        BungeePlayer bungeePlayer = new BungeePlayer(player.getUniqueId().toString(), player.getName());
        Common.getInstance().getOnlinePlayers().put(player.getUniqueId().toString(), bungeePlayer);
    }

    @EventHandler
    public void onInitialJoin(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        if (!joined.contains(uuid)) {
            joined.add(uuid);

            ServerObject lobby = ServerHandler.getFreeServer(ServerType.MAIN_LOBBY);
            if (lobby != null) {
                ServerInfo lobbyObj = ProxyServer.getInstance().constructServerInfo(
                        lobby.getServerName(),
                        new InetSocketAddress(lobby.getServerIp(), lobby.getServerPort()),
                        lobby.getServerName(),
                        false);

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

        // Remove player once they are gone
        Map<String, AbstractPlayer> players = Common.getInstance().getOnlinePlayers();
        players.get(player.getUniqueId().toString()).cleanup();
        players.remove(player.getUniqueId().toString());
    }
}
