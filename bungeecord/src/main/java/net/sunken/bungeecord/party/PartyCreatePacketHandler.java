package net.sunken.bungeecord.party;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.PartyCreatePacket;
import net.sunken.common.parties.service.status.PartyCreateStatus;

public class PartyCreatePacketHandler extends PacketHandler<PartyCreatePacket> {

    @Override
    public void onReceive(PartyCreatePacket packet) {
        PartyPlayer creator = packet.getCreator();
        PartyPlayer invitee = packet.getInvitee();
        PartyCreateStatus createStatus = packet.getCreateStatus();

        ProxiedPlayer creatorPlayer = ProxyServer.getInstance().getPlayer(creator.getUniqueId());
        ProxiedPlayer toInvitePlayer = ProxyServer.getInstance().getPlayer(invitee.getUniqueId());

        switch (createStatus) {
            case SUCCESS:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(
                            new TextComponent("You created a party with" + invitee.getName() + "!"));
                }
                if (toInvitePlayer != null) {
                    toInvitePlayer.sendMessage(
                            new TextComponent("You are now in a party with " + creator.getName() + "!"));
                }
                break;
            case FAILED:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("Could not create the party!"));
                }
                break;
            default:
                assert false : "unknown field";
        }
    }
}
