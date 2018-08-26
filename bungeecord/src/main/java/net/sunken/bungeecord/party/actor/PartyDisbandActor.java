package net.sunken.bungeecord.party.actor;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.changes.PartyDisbandedPacket;

/** Act on an invite expired packet */
public class PartyDisbandActor extends PacketHandler<PartyDisbandedPacket> {

    @Override
    public void onReceive(PartyDisbandedPacket packet) {
        Party partyDeleted = packet.getPartyDeleted();
        for (PartyPlayer member : partyDeleted.getAllMembers()) {
            ProxiedPlayer memberPlayer = ProxyServer.getInstance().getPlayer(member.getUniqueId());
            if (memberPlayer != null) {
                memberPlayer.sendMessage(new TextComponent("The party has been disbanded!"));
            }
        }
    }
}
