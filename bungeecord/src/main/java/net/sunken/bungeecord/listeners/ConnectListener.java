package net.sunken.bungeecord.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.sunken.bungeecord.Constants;
import net.sunken.bungeecord.server.ServerHandler;
import net.sunken.bungeecord.util.MessageUtil;
import net.sunken.common.Common;
import net.sunken.common.server.data.ServerObject;
import net.sunken.common.type.ServerType;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                        Set<ServerObject> servers = Common.getInstance().getServerCache().getCache();

                        List<ServerObject> serversList = servers.stream()
                                .sorted(Comparator.comparing(ServerObject::getPlayerCount))
                                .collect(Collectors.toList());

                        for(int i = 0; i < serversList.size(); i++){
                            ServerObject serv = serversList.get(i);

                            if(serv.getServerName().equals(server)){
                                ServerInfo serverObj = ProxyServer.getInstance().constructServerInfo(
                                        serv.getServerName(),
                                        new InetSocketAddress(serv.getServerIp(), serv.getServerPort()),
                                        serv.getServerName(),
                                        false);

                                target.connect(serverObj);
                            }
                        }
                    }
                } else if (channel.equals("type")) { // Sends player to a free server of that type
                    String name = in.readUTF();
                    ServerType type = ServerType.valueOf(in.readUTF());

                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
                    if (target != null && target.isConnected() && type != null) {
                        ServerObject lobby = ServerHandler.getFreeServer(type);

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
