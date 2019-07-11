package net.sunken.master.party;

import com.google.common.collect.Sets;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.changes.PartyCheckPacket;
import net.sunken.common.parties.packet.request.MPartyCheckRequestPacket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class PartyCheckHandler extends PacketHandler<MPartyCheckRequestPacket> {

    @Override
    public void onReceive(MPartyCheckRequestPacket packet) {
        Party party = PartyManager.getPartyByPlayer(packet.getRequester());

        boolean inParty = party != null;
        Set<PartyPlayer> members = Sets.newHashSet();
        Collection<UUID> invites = new ArrayList<>();

        //--- If the party exists, populate arrays.
        if (party != null) {
            members = party.getAllMembers();
            invites = PartyInviteManager.getInvites(party.getLeaderUniqueId());
        }

        //--- Return data back to servers.
        PacketUtil.sendPacket(new PartyCheckPacket(
                packet.getRequester(),
                inParty,
                members,
                invites
        ));
    }
}

