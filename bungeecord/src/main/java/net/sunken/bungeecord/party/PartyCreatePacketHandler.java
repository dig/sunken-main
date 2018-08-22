package net.sunken.bungeecord.party;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.parties.packet.PartyCreatePacket;
import net.sunken.common.parties.service.status.PartyCreateStatus;
import net.sunken.common.util.PlayerDetail;

public class PartyCreatePacketHandler extends PacketHandler<PartyCreatePacket> {

    @Override
    public void onReceive(PartyCreatePacket packet) {
        PlayerDetail creator = packet.getCreator();
        PlayerDetail invitee = packet.getInvitee();
        PartyCreateStatus createStatus = packet.getCreateStatus();

        ProxiedPlayer creatorPlayer = ProxyServer.getInstance().getPlayer(creator.uuid);
        ProxiedPlayer toInvitePlayer = ProxyServer.getInstance().getPlayer(invitee.uuid);

        switch (createStatus) {
            case INVITER_ALREADY_IN_PARTY:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("You are already in a party!"));
                }
                break;
            case INVITEE_ALREADY_IN_PARTY:
                if (toInvitePlayer != null) {
                    toInvitePlayer.sendMessage(new TextComponent("That player is already in a party!"));
                }
                break;
            case SUCCESS:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(
                            new TextComponent("You invited " + invitee.name + " to a party!"));
                }
                if (toInvitePlayer != null) {
                    toInvitePlayer.sendMessage(
                            new TextComponent("You received a party request from " + creator.name + "!"));
                }
                break;
            case FAILED:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("Could not send party request..."));
                }
                break;
            default:
                assert false : "unknown field";
        }
    }
}
