package net.sunken.bungeecord.packet;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.parties.packet.PartyInviteExpiredPacket;

public class PartyInviteExpiredHandler extends PlayerPacketHandler<PartyInviteExpiredPacket> {

    @Override
    public void onPlayerPacketReceive(PartyInviteExpiredPacket packet, ProxiedPlayer player) {
        player.sendMessage(new TextComponent("Your party invite to " + packet.getAttemptedInviteName() + " expired!"));
    }
}
