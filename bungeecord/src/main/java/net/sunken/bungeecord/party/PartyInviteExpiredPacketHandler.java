package net.sunken.bungeecord.party;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.bungeecord.util.PlayerPacketHandler;
import net.sunken.common.parties.packet.PartyInviteExpiredPacket;

public class PartyInviteExpiredPacketHandler extends PlayerPacketHandler<PartyInviteExpiredPacket> {

    @Override
    public void onPlayerPacketReceive(PartyInviteExpiredPacket packet, ProxiedPlayer player) {
        player.sendMessage(new TextComponent("Your party invite to " + packet.getAttemptedInviteName() + " expired!"));
    }
}