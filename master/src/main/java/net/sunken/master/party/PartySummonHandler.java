package net.sunken.master.party;

import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.MustBeInPartyPacket;
import net.sunken.common.parties.packet.MustBeLeaderPacket;
import net.sunken.common.parties.packet.changes.PartyAttemptSummonPacket;
import net.sunken.common.parties.packet.request.MPartySummonRequestPacket;

import java.util.Set;
import java.util.UUID;

/** Requests for summoning players to a server are handled here. */
public class PartySummonHandler extends PacketHandler<MPartySummonRequestPacket> {

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
        PartyAttemptSummonPacket partyAttemptSummonPacket = new PartyAttemptSummonPacket(
                serverName,
                requester,
                allMembers);
        PacketUtil.sendPacket(partyAttemptSummonPacket);
    }
}
