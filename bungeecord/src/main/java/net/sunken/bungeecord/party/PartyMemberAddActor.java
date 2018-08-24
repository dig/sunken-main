package net.sunken.bungeecord.party;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.changes.PartyMemberJoinedPacket;

import java.util.Set;

public class PartyMemberAddActor extends PacketHandler<PartyMemberJoinedPacket> {

    @Override
    public void onReceive(PartyMemberJoinedPacket packet) {
        PartyPlayer memberJoined = packet.getMemberJoined();
        Party party = packet.getParty();

        ProxiedPlayer memberJoinedPlayer = ProxyServer.getInstance().getPlayer(memberJoined.getUniqueId());
        if (memberJoinedPlayer != null) {
            memberJoinedPlayer.sendMessage(
                    new TextComponent("You joined " + party.getLeader().getName() + "'s party!"));
        }

        Set<PartyPlayer> allMembers = party.getAllMembers();
        for (PartyPlayer member : allMembers) {
            if (!member.equals(memberJoined)) { // don't send to the member that joined
                ProxiedPlayer memberPlayer = ProxyServer.getInstance().getPlayer(member.getUniqueId());
                if (memberPlayer != null) {
                    memberPlayer.sendMessage(new TextComponent(memberJoined.getName() + "has joined the party!"));
                }
            }
        }
    }
}
