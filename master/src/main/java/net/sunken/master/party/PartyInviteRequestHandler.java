package net.sunken.master.party;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.PartyCreatePacket;
import net.sunken.common.parties.packet.PartyInviteSendPacket;
import net.sunken.common.parties.packet.PartyInviteValidatePacket;
import net.sunken.common.parties.service.PartyService;
import net.sunken.common.parties.service.status.PartyCreateStatus;
import net.sunken.common.parties.service.status.PartyInviteStatus;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.util.PlayerDetail;

import java.util.Map;
import java.util.UUID;

/**
 * When a request to send an invite is received i.e. /party (player), this
 * handler decides what to do next. It acts as a hub for invites requests.
 */
public class PartyInviteRequestHandler extends PacketHandler<PartyInviteSendPacket> {

    private static DataManager dataManager;
    private static Map<UUID, AbstractPlayer> onlinePlayers;
    private static PartyService partyService;

    static {
        dataManager = Common.getInstance().getDataManager();
        partyService = Common.getInstance().getPartyService();
        onlinePlayers = dataManager.getOnlinePlayers();
    }

    @Override
    public void onReceive(PartyInviteSendPacket packet) {
        UUID creator = packet.getCreator();
        String toInviteName = packet.getToInvite();

        AbstractPlayer creatorPlayer = onlinePlayers.get(creator);
        UUID toInvite = dataManager.getNameToUUID().get(toInviteName.toLowerCase());
        AbstractPlayer toInvitePlayer = null;
        if (toInvite != null) {
            toInvitePlayer = onlinePlayers.get(toInvite);
        }

        if (creatorPlayer != null && toInvite != null) {
            // check to see whether the person being invited has already
            // sent a request to the creator in which case, create the party
            if (PartyInviteManager.hasInvited(toInvite, creator)) {
                PartyPlayer leader = new PartyPlayer(creator, creatorPlayer.getName(), creatorPlayer.getRank());
                PartyPlayer invitee = new PartyPlayer(toInvite, toInvitePlayer.getName(), toInvitePlayer.getRank());
                PartyCreateStatus createStatus = partyService.createParty(leader, invitee);

                PartyCreatePacket partyCreatePacket = new PartyCreatePacket(leader, invitee, createStatus);
                PacketUtil.sendPacket(partyCreatePacket);
                return;
            }

            PartyInviteStatus partyInviteStatus = partyService.validateInviteRequest(creator, toInvite);
            // make sure after the standard validation checks have ran, there isn't
            // an invite already pending by the user to the same person
            if (PartyInviteManager.hasInvited(creator, toInvite)) {
                partyInviteStatus = PartyInviteStatus.INVITE_ALREADY_PENDING;
            }
            // success, add the invite
            if (partyInviteStatus == PartyInviteStatus.SUCCESS) {
                PartyInviteManager.addInvite(creator, toInvite, toInvitePlayer.getName());
            }
            PartyInviteValidatePacket partyInviteValidatePacket = new PartyInviteValidatePacket(
                    new PlayerDetail(creatorPlayer.getUUID(), creatorPlayer.getName()),
                    new PlayerDetail(toInvitePlayer.getUUID(), toInvitePlayer.getName()),
                    partyInviteStatus);
            PacketUtil.sendPacket(partyInviteValidatePacket);
        }
    }
}
