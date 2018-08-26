package net.sunken.bungeecord.party.actor;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.bungeecord.util.PlayerPacketHandler;
import net.sunken.common.parties.packet.MustBeLeaderPacket;

/** Act on a packet requesting the player to be the leader of the party */
public class MustBeLeaderActor extends PlayerPacketHandler<MustBeLeaderPacket> {

    @Override
    public void onPlayerPacketReceive(MustBeLeaderPacket packet, ProxiedPlayer player) {
        player.sendMessage(new TextComponent(packet.getMessage()));
    }
}
