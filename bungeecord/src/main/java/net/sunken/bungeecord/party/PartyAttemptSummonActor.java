package net.sunken.bungeecord.party;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.bungeecord.server.ServerHandler;
import net.sunken.common.Common;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.changes.PartyAttemptSummonPacket;
import net.sunken.common.server.ServerObjectCache;
import net.sunken.common.server.data.ServerObject;

import java.util.Set;
import java.util.UUID;

public class PartyAttemptSummonActor extends PacketHandler<PartyAttemptSummonPacket> {

    private static final ServerObjectCache serverCache = Common.getInstance().getServerCache();

    @Override
    public void onReceive(PartyAttemptSummonPacket packet) {
        String serverName = packet.getServerName();
        UUID requestingSummon = packet.getRequestingSummon();
        Set<PartyPlayer> partyPlayers = packet.getPartyPlayers();

        for (ServerObject serverObject : serverCache.getCache()) {
            if (serverObject.getServerName().equals(serverName)) {
                for (PartyPlayer partyPlayer : partyPlayers) {
                    UUID partyPlayerUUID = partyPlayer.getUniqueId();
                    if (!partyPlayerUUID.equals(requestingSummon)) {
                        ProxiedPlayer bungeePartyPlayer = ProxyServer.getInstance().getPlayer(partyPlayerUUID);
                        if (bungeePartyPlayer != null) {
                            ServerHandler.sendPlayerToServer(bungeePartyPlayer, serverObject);
                        }
                    }
                }
                break;
            }
        }
    }
}
