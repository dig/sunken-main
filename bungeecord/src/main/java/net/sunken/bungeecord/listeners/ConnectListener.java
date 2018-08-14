package net.sunken.bungeecord.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.sunken.bungeecord.Constants;
import net.sunken.bungeecord.lobby.LobbyHandler;
import net.sunken.bungeecord.util.MessageUtil;
import net.sunken.common.server.ServerObject;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ConnectListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (event.getTag().equals("BungeeCord") && event.getSender() instanceof Server) {
            ByteArrayInputStream stream = new ByteArrayInputStream(event.getData());
            DataInputStream in = new DataInputStream(stream);
            String channel;

            try {
                channel = in.readUTF();

                // Connect to specified server
                if (channel.equals("server")) {
                    String name = in.readUTF();
                    String server = in.readUTF();
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
                    if (target != null && target.isConnected()) {
                        // TODO: Check local cache then check redis for server and connect
                    }
                } else if (channel.equals("lobby")) { // Connect to a free lobby
                    String name = in.readUTF();

                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
                    if (target != null && target.isConnected()) {
                        ServerObject lobby = LobbyHandler.getFreeLobby();

                        if (lobby != null) {
                            ServerInfo lobbyObj = ProxyServer.getInstance().constructServerInfo(
                                    lobby.getServerName(),
                                    new InetSocketAddress(lobby.getServerIp(), lobby.getServerPort()),
                                    lobby.getServerName(),
                                    false);

                            target.connect(lobbyObj);
                        } else {
                            target.disconnect(MessageUtil.stringToComponent(Constants.NO_LOBBY));
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
