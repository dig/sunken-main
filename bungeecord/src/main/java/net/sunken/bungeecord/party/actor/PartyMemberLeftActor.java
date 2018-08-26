package net.sunken.bungeecord.party.actor;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.changes.PartyMemberLeftPacket;

import java.util.UUID;

/** Act on a member left packet */
public class PartyMemberLeftActor extends PacketHandler<PartyMemberLeftPacket> {

    @Override
    public void onReceive(PartyMemberLeftPacket packet) {
        UUID memberLeftUUID = packet.getMemberLeftUUID();
        Party party = packet.getParty();

        ProxiedPlayer memberLeft = ProxyServer.getInstance().getPlayer(memberLeftUUID);

        if (memberLeft != null) {
            memberLeft.sendMessage(new TextComponent("You left the party!"));
        }

        for (PartyPlayer member : party.getAllMembers()) {
            ProxiedPlayer memberPlayer = ProxyServer.getInstance().getPlayer(member.getUniqueId());
            if (memberPlayer != null && !member.getUniqueId().equals(memberLeftUUID)) {
                memberPlayer.sendMessage(new TextComponent(packet.getNameLeft() + " left the party!"));
            }
        }
    }
}
