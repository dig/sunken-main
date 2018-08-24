package net.sunken.master.party;

import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.MustBeInPartyPacket;
import net.sunken.common.parties.packet.changes.PartyListPacket;
import net.sunken.common.parties.packet.request.MPartyLeaveRequestPacket;

import java.util.Set;
import java.util.UUID;

/** Requests for listing party players are handled here. */
public class PartyListHandler extends PacketHandler<MPartyLeaveRequestPacket> {

    @Override
    public void onReceive(MPartyLeaveRequestPacket packet) {
        UUID leaverUUID = packet.getLeaver();

        Party party = PartyManager.getPartyByPlayer(leaverUUID);
        if (party == null) {
            MustBeInPartyPacket mustBeInPartyPacket = new MustBeInPartyPacket(leaverUUID);
            PacketUtil.sendPacket(mustBeInPartyPacket);
            return;
        }

        Set<PartyPlayer> allMembers = party.getAllMembers();
        PartyListPacket partyListPacket = new PartyListPacket(leaverUUID, allMembers);
        PacketUtil.sendPacket(partyListPacket);
    }
}
