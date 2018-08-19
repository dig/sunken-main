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
        UUID creatorUUID = packet.getCreator();
        UUID toUUID = packet.getTo();

        ProxiedPlayer creator = ProxyServer.getInstance().getPlayer(creatorUUID);
        ProxiedPlayer invitee = ProxyServer.getInstance().getPlayer(toUUID);

        if (creator != null && invitee != null) {
            creator.sendMessage(new TextComponent("You sent a party invite to " + invitee.getName()));
            invitee.sendMessage(new TextComponent("You received a party invite from " + creator.getName()));
        }
    }
}
