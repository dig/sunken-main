package net.sunken.bungeecord;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.bungeecord.util.PlayerPacketHandler;
import net.sunken.common.packet.SendPlayerBungeeMessagePacket;

/** Interpret the send message packet and do the message sending */
public class SendPlayerBungeeMessageHandler extends PlayerPacketHandler<SendPlayerBungeeMessagePacket> {

    @Override
    public void onPlayerPacketReceive(SendPlayerBungeeMessagePacket packet, ProxiedPlayer player) {
        player.sendMessage(new TextComponent(packet.getFormattedMessage()));
    }
}
