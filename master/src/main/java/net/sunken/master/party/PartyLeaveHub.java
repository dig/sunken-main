package net.sunken.master.party;

import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.packet.MustBeInPartyPacket;
import net.sunken.common.parties.packet.MustBeLeaderPacket;
import net.sunken.common.parties.packet.changes.PartyDisbandedPacket;
import net.sunken.common.parties.packet.changes.PartyMemberLeftPacket;
import net.sunken.common.parties.packet.request.MPartyLeaveRequestPacket;

import java.util.UUID;

/**
 * The leave hub. This will receive oncoming leave request packets
 * and decide what to do with each case i.e. if the player is a leader,
 * disband; if the player is a member, simply leave the party. Has
 * sanity checks.
 */
public class PartyLeaveHub extends PacketHandler<MPartyLeaveRequestPacket> {

    @Override
    public void onReceive(MPartyLeaveRequestPacket packet) {
        UUID leaverUUID = packet.getLeaver();

        boolean isSuccessfulLeave = false;

        Party asLeader = PartyManager.getPartyByLeader(leaverUUID);

        if (asLeader != null) {
            PartyManager.deleteParty(asLeader);

            PartyDisbandedPacket partyDisbandedPacket = new PartyDisbandedPacket(asLeader);
            PacketUtil.sendPacket(partyDisbandedPacket);

            isSuccessfulLeave = true;
        } else {
            Party asMember = PartyManager.getPartyByPlayer(leaverUUID);
            if (asMember != null) {
                PartyMemberLeftPacket partyMemberLeftPacket = new PartyMemberLeftPacket(leaverUUID, asMember);
                PacketUtil.sendPacket(partyMemberLeftPacket);

                isSuccessfulLeave = true;
            }
        }

        // not a leader of a party
        if (packet.isDisbanding() && asLeader == null) {
            PacketUtil.sendPacket(new MustBeLeaderPacket(leaverUUID, "You must be the leader to disband a party!"));
            return;
        }

        // leaver is not in a party
        if (!isSuccessfulLeave) {
            PacketUtil.sendPacket(new MustBeInPartyPacket(leaverUUID));
        }
    }
}
