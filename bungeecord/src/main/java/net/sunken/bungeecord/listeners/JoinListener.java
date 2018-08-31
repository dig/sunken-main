package net.sunken.bungeecord.listeners;

import com.google.common.collect.ImmutableMap;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.sunken.bungeecord.BungeeMain;
import net.sunken.bungeecord.Constants;
import net.sunken.bungeecord.player.BungeePlayer;
import net.sunken.bungeecord.server.ServerHandler;
import net.sunken.bungeecord.util.MessageUtil;
import net.sunken.common.Common;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.player.PlayerRank;
import net.sunken.common.player.packet.PlayerConnectPacket;
import net.sunken.common.player.packet.PlayerJoinPacket;
import net.sunken.common.player.packet.PlayerQuitPacket;
import net.sunken.common.server.data.ServerObject;
import net.sunken.common.type.ServerType;
import org.bson.Document;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class JoinListener implements Listener {

    private ArrayList<String> joined;

    public JoinListener () {
        this.joined = new ArrayList<>();
    }

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
    public void onFinalJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        // Correct collection for getting player data
        MongoCollection<Document> playerCollection = Common.getInstance()
                .getMongo()
                .getConnection()
                .getDatabase(DatabaseConstants.DATABASE_NAME)
                .getCollection(DatabaseConstants.PLAYER_COLLECTION);

        // Get player document
        Document document = playerCollection.find(Filters.eq(DatabaseConstants.PLAYER_UUID_FIELD,
                player.getUniqueId().toString())).first();
        boolean firstJoin = false;

        if (document == null) {
            Document playerDocument = new Document(ImmutableMap.of(
                    DatabaseConstants.PLAYER_UUID_FIELD, player.getUniqueId().toString(),
                    DatabaseConstants.PLAYER_NAME_FIELD, player.getName(),
                    DatabaseConstants.PLAYER_RANK_FIELD, PlayerRank.USER.toString(),
                    DatabaseConstants.PLAYER_ACHIEVEMENTS_FIELD, new ArrayList<Document>()
            ));

            playerCollection.insertOne(playerDocument);

            document = playerDocument;
            firstJoin = true;
        }

        // Add player to the network
        PacketUtil.sendPacket(new PlayerJoinPacket(player.getName(), player.getUniqueId(),
                document, firstJoin));

        // Add to local cache
        BungeePlayer bungeePlayer = new BungeePlayer(player.getUniqueId().toString(), player.getName(),
                document, firstJoin);

        Common.getInstance()
                .getDataManager()
                .getOnlinePlayers()
                .put(player.getUniqueId(), bungeePlayer);

        // Network join message
        for (String message : Constants.JOIN_MESSAGES) {
            MessageUtil.sendMessage(player, message);
        }
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

        if (!event.isCancelled() && event.getTarget() != null) {
            // Update our network because player is joining new server
            PacketUtil.sendPacket(new PlayerConnectPacket(player.getUniqueId(), event.getTarget().getName()));
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        joined.remove(player.getUniqueId().toString());

        // Remove player from our network
        PacketUtil.sendPacket(new PlayerQuitPacket(player.getName(), player.getUniqueId()));

        // Remove player from local cache
        Map<UUID, AbstractPlayer> players = Common.getInstance()
                .getDataManager()
                .getOnlinePlayers();

        players.get(player.getUniqueId()).cleanup();
        players.remove(player.getUniqueId());
    }
}
