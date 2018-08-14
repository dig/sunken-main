package net.sunken.bungeecord.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectFailEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.sunken.bungeecord.Constants;
import net.sunken.bungeecord.server.ServerHandler;
import net.sunken.bungeecord.util.MessageUtil;
import net.sunken.common.Common;
import net.sunken.common.server.ServerObject;
import net.sunken.common.type.ServerType;

import java.net.InetSocketAddress;
import java.util.logging.Level;

public class FailListener implements Listener {

    @EventHandler
    public void onServerFail(ServerConnectFailEvent event) {
        event.setSendMessage(true);

        ServerObject lobby = ServerHandler.getFreeServer(ServerType.MAIN_LOBBY);
        if(lobby != null){
            ServerInfo lobbyObj = ProxyServer.getInstance().constructServerInfo(
                    lobby.getServerName(),
                    new InetSocketAddress(lobby.getServerIp(), lobby.getServerPort()),
                    lobby.getServerName(),
                    false);

            Common.getLogger().log(Level.INFO, "Fallbacking " + event.getPlayer().getName() + " to lobby " + lobby.getServerName()
                    + " (" + lobby.getServerIp() + ")");

            event.setFallbackServer(lobbyObj);
        } else {
            event.getPlayer().disconnect(MessageUtil.stringToComponent(Constants.NO_LOBBY));
        }
    }

}
