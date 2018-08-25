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
import net.sunken.common.parties.packet.request.MPartyKickRequestPacket;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/** Requests for kicking party players are handled here. */
public class PartyKickHandler extends PacketHandler<MPartyKickRequestPacket> {

    private static final DataManager dataManager = Common.getInstance().getDataManager();

    @Override
    public void onReceive(MPartyKickRequestPacket packet) {
        UUID requestingKick = packet.getRequestingKick();

        Party party = PartyManager.getPartyByPlayer(requestingKick);
        if (party == null) {
            PacketUtil.sendPacket(new MustBeInPartyPacket(requestingKick));
            return;
        }

        if (!party.getLeaderUniqueId().equals(requestingKick)) {
            PacketUtil.sendPacket(new MustBeLeaderPacket(
                    requestingKick, "You must be the leader in order to kick players from the party."));
            return;
        }

        Set<PartyPlayer> allMembers = party.getAllMembers();

        if (!packet.isRequestingKickOffline()) {
            String toKick = packet.getToKick();
            UUID toKickUUID = dataManager.getNameToUUID().get(toKick);

            boolean foundAndRemoved = allMembers.remove(PartyPlayer.fromUUID(toKickUUID));
            if (!foundAndRemoved) {
                PacketUtil.sendPacket(new SendPlayerBungeeMessagePacket(
                        requestingKick, "Player is not in the party!"));
            } else {
                PacketUtil.sendPacket(
                        new SendPlayerBungeeMessagePacket(toKickUUID, "You have been kicked from the party!"));
            }
        } else {
            boolean hasKickedAnyone = false;
            for (Iterator<PartyPlayer> iterator = allMembers.iterator(); iterator.hasNext(); ) {
                PartyPlayer member = iterator.next();
                UUID memberUUID = member.getUniqueId();
                // discard leader from kicking though it shouldn't make
                // much difference as they should be online when doing the command
                if (!memberUUID.equals(requestingKick)) {
                    if (dataManager.getOnlinePlayers().containsKey(memberUUID)) {
                        iterator.remove();
                        hasKickedAnyone = true;
                    }
                }
            }

            if (hasKickedAnyone) {
                PacketUtil.sendPacket(new SendPlayerBungeeMessagePacket(
                        requestingKick, "Kicked all offline party members."));
            } else {
                PacketUtil.sendPacket(new SendPlayerBungeeMessagePacket(
                        requestingKick, "No offline party members to kick!"));
            }
        }
    }
}
