package net.sunken.master.party;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.packet.SendPlayerBungeeMessagePacket;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.MustBeInPartyPacket;
import net.sunken.common.parties.packet.MustBeLeaderPacket;
import net.sunken.common.parties.packet.request.MPartyPromoteRequestPacket;

import java.util.UUID;

/** Requests for promoting party players are handled here. */
public class PartyPromoteHandler extends PacketHandler<MPartyPromoteRequestPacket> {

    private static final DataManager dataManager = Common.getInstance().getDataManager();

    @Override
    public void onReceive(MPartyPromoteRequestPacket packet) {
        UUID requestingPromote = packet.getRequester();
        String toPromoteName = packet.getToPromote();
        UUID toPromoteUUID = dataManager.getNameToUUID().get(toPromoteName.toLowerCase());

        Party party = PartyManager.getPartyByPlayer(requestingPromote);
        if (party == null) {
            PacketUtil.sendPacket(new MustBeInPartyPacket(requestingPromote));
            return;
        }

        if (!party.getLeaderUniqueId().equals(requestingPromote)) {
            PacketUtil.sendPacket(new MustBeLeaderPacket(
                    requestingPromote, "You must be the leader in order to promote a party member."));
            return;
        }

        if (!party.getAllMembers().contains(PartyPlayer.fromUUID(toPromoteUUID))) {
            PacketUtil.sendPacket(new MustBeInPartyPacket(requestingPromote));
            return;
        }

        // delete the original
        PartyManager.deleteParty(party);
        // set the new UUID of the leader
        party.setLeaderUniqueId(toPromoteUUID);
        // put the new party back with the new leader key UUID
        PartyManager.putParty(party);

        PacketUtil.sendPacket(new SendPlayerBungeeMessagePacket(requestingPromote,
                "Successfully promoted " + toPromoteName + " to party leader!"));
        PacketUtil.sendPacket(new SendPlayerBungeeMessagePacket(toPromoteUUID, "You are now the party leader!"));
    }
}
