package net.sunken.bungeecord.party;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.parties.packet.PartyInviteSendPacket;

import java.util.UUID;

public class PartyInviteSendPacketHandler extends PacketHandler<PartyInviteSendPacket> {

    @Override
    public void onReceive(PartyInviteSendPacket packet) {
        UUID creator = packet.getCreator();
        String toInvite = packet.getToInvite();

        ProxiedPlayer creatorPlayer = ProxyServer.getInstance().getPlayer(creator);
        ProxiedPlayer inviteePlayer = ProxyServer.getInstance().getPlayer(toInvite);

        if (creatorPlayer != null) {
            creatorPlayer.sendMessage(new TextComponent("You sent a party invite to " + inviteePlayer.getName()));
        }

        if (inviteePlayer != null) {
            inviteePlayer.sendMessage(new TextComponent("You received a party invite from " + creatorPlayer.getName()));
        }
    }
}
