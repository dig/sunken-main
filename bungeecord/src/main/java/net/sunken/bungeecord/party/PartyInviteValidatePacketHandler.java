package net.sunken.bungeecord.party;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.parties.packet.PartyInviteValidatePacket;
import net.sunken.common.parties.service.status.PartyInviteStatus;
import net.sunken.common.util.PlayerDetail;

public class PartyInviteValidatePacketHandler extends PacketHandler<PartyInviteValidatePacket> {

    @Override
    public void onReceive(PartyInviteValidatePacket packet) {
        PlayerDetail creator = packet.getCreator();
        PlayerDetail toInvite = packet.getToInvite();
        PartyInviteStatus inviteStatus = packet.getPartyInviteStatus();

        ProxiedPlayer creatorPlayer = ProxyServer.getInstance().getPlayer(creator.uuid);
        ProxiedPlayer toInvitePlayer = ProxyServer.getInstance().getPlayer(toInvite.uuid);

        switch (inviteStatus) {
            case SUCCESS:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("You invited " + toInvite.name + " to a party!"));
                }
                if (toInvitePlayer != null) {
                    toInvitePlayer.sendMessage(new TextComponent("You received an invite from " + creator.name));
                }
                break;
            case FAILED:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("Could not send party request..."));
                }
                break;
            case INVITER_ALREADY_IN_PARTY:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("You are already in a party!"));
                }
                break;
            case INVITEE_ALREADY_IN_PARTY:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("That player is already in a party!"));
                }
                break;
            default:
                assert false : "unknown field";
        }
    }
}
