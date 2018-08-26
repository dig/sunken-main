package net.sunken.master.party;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.MustBeInPartyPacket;
import net.sunken.common.parties.packet.MustBeLeaderPacket;
import net.sunken.common.parties.packet.changes.PartyAttemptSummonPacket;
import net.sunken.common.parties.packet.request.MPartySummonRequestPacket;
import net.sunken.common.server.ServerObjectCache;
import net.sunken.common.server.data.ServerObject;
import net.sunken.master.player.MasterPlayer;

import java.util.Set;
import java.util.UUID;

/** Requests for summoning players to a server are handled here. */
public class PartySummonHandler extends PacketHandler<MPartySummonRequestPacket> {

    private static final DataManager dataManager = Common.getInstance().getDataManager();
    private static final ServerObjectCache serverCache = Common.getInstance().getServerCache();

    @Override
    public void onReceive(MPartySummonRequestPacket packet) {
        UUID requester = packet.getRequester();
        String serverName = packet.getServerName();

        Party party = PartyManager.getPartyByPlayer(requester);
        if (party == null) {
            PacketUtil.sendPacket(new MustBeInPartyPacket(requester));
            return;
        }

        if (!party.getLeaderUniqueId().equals(requester)) {
            PacketUtil.sendPacket(new MustBeLeaderPacket(
                    requester, "You must be the leader in order to summon party members to your server."));
            return;
        }

        Set<PartyPlayer> allMembers = party.getAllMembers();

        // Calculate online members
        int onlineMembers = 0;
        for (PartyPlayer player : allMembers) {
            if (dataManager.getOnlinePlayers().containsKey(player.getUniqueId())) {
                onlineMembers++;
            }
        }

        // Get server by name
        ServerObject server = null;
        for (ServerObject serv : serverCache.getCache()) {
            if (serv.getServerName().equals(serverName)) {
                server = serv;
            }
        }

        // Checking if the server cannot fit the party, removing the one because
        // of leader is already on the target server. max - count = slots available.
        if ((server.getMaxPlayers() - server.getPlayerCount()) < (onlineMembers - 1)) {
            PacketUtil.sendPacket(new MustBeLeaderPacket(
                    requester, "Unable to summon party, theres not enough room on your server!"));
            return;
        }

        PartyAttemptSummonPacket partyAttemptSummonPacket = new PartyAttemptSummonPacket(
                serverName,
                requester,
                allMembers);
        PacketUtil.sendPacket(partyAttemptSummonPacket);
    }
}
