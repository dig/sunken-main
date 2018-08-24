package net.sunken.master.party;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.MustBeLeaderPacket;
import net.sunken.common.parties.packet.changes.PartyCreatedPacket;
import net.sunken.common.parties.packet.changes.PartyInviteValidatedPacket;
import net.sunken.common.parties.packet.changes.PartyMemberJoinedPacket;
import net.sunken.common.parties.packet.request.MPartyInviteRequestPacket;
import net.sunken.common.parties.status.PartyCreateStatus;
import net.sunken.common.parties.status.PartyInviteStatus;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.util.PlayerDetail;

import java.util.Map;
import java.util.UUID;

/**
 * When a request to send an invite is received i.e. /party (player), this
 * handler decides what to do next. It acts as a hub for invites requests.
 */
public class PartyInviteRequestHub extends PacketHandler<MPartyInviteRequestPacket> {

    private static DataManager dataManager;
    private static Map<UUID, AbstractPlayer> onlinePlayers;

    static {
        dataManager = Common.getInstance().getDataManager();
        onlinePlayers = dataManager.getOnlinePlayers();
    }

    @Override
    public void onReceive(MPartyInviteRequestPacket packet) {
        UUID creator = packet.getCreator();
        UUID invitee = dataManager.getNameToUUID().get(packet.getInvitee().toLowerCase());

        AbstractPlayer creatorPlayer = onlinePlayers.get(creator);
        AbstractPlayer toInvitePlayer = null;
        if (invitee != null) {
            toInvitePlayer = onlinePlayers.get(invitee);
        }

        if (creatorPlayer != null && invitee != null) {
            PlayerDetail creatorDetail = new PlayerDetail(creator, creatorPlayer.getName());
            PlayerDetail inviteeDetail = new PlayerDetail(invitee, toInvitePlayer.getName());

            PartyPlayer creatorPartyPlayer = new PartyPlayer(
                    creator,
                    creatorPlayer.getName(),
                    creatorPlayer.getRank());
            PartyPlayer inviteePartyPlayer = new PartyPlayer(
                    invitee,
                    toInvitePlayer.getName(),
                    toInvitePlayer.getRank());

            PartyInviteStatus partyInviteStatus = PartyInviteManager.validateInviteRequest(creator, invitee);

            if (partyInviteStatus == PartyInviteStatus.NOT_LEADER) {
                PacketUtil.sendPacket(
                        new MustBeLeaderPacket(creator, "You must be the leader to invite players to the party!"));
                return;
            }

            // Check special cases first //

            // check to see whether the person being invited has already
            // sent a request to the creator in which case, create the party
            // as long as there is no existing party with either player in it
            if (PartyInviteManager.isInvitePresent(invitee, creator)
                    && PartyManager.getPartyByPlayer(creator) == null
                    && PartyManager.getPartyByPlayer(invitee) == null) {

                PartyInviteManager.removeInvite(invitee, creator);
                PartyCreateStatus createStatus = PartyManager.createParty(creatorPartyPlayer, inviteePartyPlayer);
                PacketUtil.sendPacket(new PartyCreatedPacket(creatorPartyPlayer, inviteePartyPlayer, createStatus));
                return;
            }

            // if the inviter is already in a party and there is already
            // an invite present between them, add the invitee as a member
            Party creatorParty = PartyManager.getPartyByLeader(creator);
            if (PartyInviteManager.isInvitePresent(invitee, creator) && creatorParty != null) {
                creatorParty.getAllMembers().add(inviteePartyPlayer);
                PacketUtil.sendPacket(new PartyMemberJoinedPacket(inviteePartyPlayer, creatorParty));
                return;
            }

            // Is a regular invite //

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
