package net.sunken.bungeecord.party;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.parties.packet.changes.PartyInviteExpiredPacket;
import net.sunken.common.util.PlayerDetail;

/** Act on an invite expired packet */
public class PartyInviteExpiredActor extends PacketHandler<PartyInviteExpiredPacket> {

    @Override
    public void onReceive(PartyInviteExpiredPacket packet) {
        PlayerDetail inviter = packet.getInviter();
        PlayerDetail invitee = packet.getInvitee();
        ProxiedPlayer inviterPlayer = ProxyServer.getInstance().getPlayer(inviter.uuid);
        ProxiedPlayer inviteePlayer = ProxyServer.getInstance().getPlayer(invitee.uuid);

        if (inviterPlayer != null) {
            inviterPlayer.sendMessage(new TextComponent("Your party invite to " + invitee.name + " has expired!"));
        }
        if (inviteePlayer != null) {
            inviteePlayer.sendMessage(new TextComponent("Party invite from " + inviter.name + " has expired!"));
        }
    }
}
