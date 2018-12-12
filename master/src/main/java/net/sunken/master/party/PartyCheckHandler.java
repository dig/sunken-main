package net.sunken.master.party;

import com.google.common.collect.Sets;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.changes.PartyCheckPacket;
import net.sunken.common.parties.packet.request.MPartyCheckRequestPacket;

import java.util.Set;
import java.util.UUID;

public class PartyCheckHandler extends PacketHandler<MPartyCheckRequestPacket> {

    @Override
    public void onReceive(MPartyCheckRequestPacket packet) {
        Party party = PartyManager.getPartyByPlayer(packet.getRequester());

        boolean inParty = party != null;
        Set<PartyPlayer> playersInParty = Sets.newHashSet();
        if (inParty) {
            playersInParty = party.getAllMembers();
        }

        PacketUtil.sendPacket(new PartyCheckPacket(
                packet.getRequester(),
                inParty,
                playersInParty,
                PartyInviteManager.getInvites(party.getLeaderUniqueId())
        ));
    }
}

