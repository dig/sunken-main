package net.sunken.bungeecord.party.actor;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.bungeecord.util.PlayerPacketHandler;
import net.sunken.common.parties.packet.MustBeInPartyPacket;

/** Act on a packet requesting the player to be in a party */
public class MustBeInPartyActor extends PlayerPacketHandler<MustBeInPartyPacket> {

    @Override
    public void onPlayerPacketReceive(MustBeInPartyPacket packet, ProxiedPlayer player) {
        player.sendMessage(new TextComponent("You must be in a party!"));
    }
}
