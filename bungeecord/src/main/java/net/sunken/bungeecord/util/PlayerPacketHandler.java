package net.sunken.bungeecord.util;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PlayerPacket;

import java.util.UUID;

public abstract class PlayerPacketHandler<E extends PlayerPacket> extends PacketHandler<E> {

    @Override
    public void onReceive(E packet) {
        UUID playerTargetedUUID = packet.getPlayerTargeted();
        ProxiedPlayer playerTargeted = ProxyServer.getInstance().getPlayer(playerTargetedUUID);
        if (playerTargeted != null) {
            this.onPlayerPacketReceive(packet, playerTargeted);
        }
    }

    public abstract void onPlayerPacketReceive(E packet, ProxiedPlayer player);
}
