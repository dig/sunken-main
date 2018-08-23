package net.sunken.master.party;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.PartyCreatedPacket;
import net.sunken.common.parties.packet.PartyInviteSendPacket;
import net.sunken.common.parties.packet.PartyInviteValidatedPacket;
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
public class PartyInviteRequestHub extends PacketHandler<PartyInviteSendPacket> {

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
        UUID toInvite = dataManager.getNameToUUID().get(packet.getToInvite().toLowerCase());

        AbstractPlayer creatorPlayer = onlinePlayers.get(creator);
        AbstractPlayer toInvitePlayer = null;
        if (toInvite != null) {
            toInvitePlayer = onlinePlayers.get(toInvite);
        }

        if (creatorPlayer != null && toInvite != null) {
            PlayerDetail creatorDetail = new PlayerDetail(creator, creatorPlayer.getName());
            PlayerDetail inviteeDetail = new PlayerDetail(toInvite, toInvitePlayer.getName());

            // check to see whether the person being invited has already
            // sent a request to the creator in which case, create the party
            if (PartyInviteManager.isInvitePresent(toInvite, creator)) {
                PartyInviteManager.removeInvite(toInvite, creator);

                PartyPlayer leader = new PartyPlayer(creator, creatorPlayer.getName(), creatorPlayer.getRank());
                PartyPlayer invitee = new PartyPlayer(toInvite, toInvitePlayer.getName(), toInvitePlayer.getRank());
                PartyCreateStatus createStatus = partyService.createParty(leader, invitee);

                PartyCreatedPacket partyCreatedPacket = new PartyCreatedPacket(leader, invitee, createStatus);
                PacketUtil.sendPacket(partyCreatedPacket);

                return;
            }

            // player is not accepting an existing party request
            // continue with the premise this is a new party that is attempting to be made
            PartyInviteStatus partyInviteStatus = partyService.validateInviteRequest(creator, toInvite);

            // make sure after the standard validation checks have ran, there isn't
            // an invite already pending by the user to the same person
            if (PartyInviteManager.isInvitePresent(creator, toInvite)) {
                partyInviteStatus = PartyInviteStatus.INVITE_ALREADY_PENDING;
            }

            // party validation checks have passed, add the invite to the manager
            if (partyInviteStatus == PartyInviteStatus.SUCCESS) {
                PartyInviteManager.addInvite(creatorDetail, inviteeDetail);
            }

            // notify receivers of the result of the party invite validation
            // so they can handle it accordingly depending on the invite status
            // and inform relevant players of this
            PartyInviteValidatedPacket partyInviteValidatedPacket = new PartyInviteValidatedPacket(
                    creatorDetail,
                    inviteeDetail,
                    partyInviteStatus);
            PacketUtil.sendPacket(partyInviteValidatedPacket);
        }
    }
}
