package net.sunken.bungeecord.party;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.bungeecord.util.PlayerPacketHandler;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.changes.PartyListPacket;

import java.util.Set;

/** Act on a party list response */
public class PartyListActor extends PlayerPacketHandler<PartyListPacket> {

    @Override
    public void onPlayerPacketReceive(PartyListPacket packet, ProxiedPlayer player) {
        Set<PartyPlayer> partyPlayers = packet.getPartyPlayers();
        for (PartyPlayer pPlayer : partyPlayers) {
            player.sendMessage(new TextComponent(pPlayer.getName()));
        }
    }
}
