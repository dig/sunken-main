package net.sunken.bungeecord.friend;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.friend.packet.FriendStatusPacket;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.util.PlayerDetail;

public class FriendStatusHandler extends PacketHandler<FriendStatusPacket> {

    @Override
    public void onReceive(FriendStatusPacket packet) {
        PlayerDetail creator = packet.getCreator();

        ProxiedPlayer creatorPlayer = ProxyServer.getInstance().getPlayer(creator.uuid);
        ProxiedPlayer invitedPlayer = ProxyServer.getInstance().getPlayer(packet.getToInvite());

        switch (packet.getStatus()) {
            case INVALID_PLAYER:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("Unable to find player '" + packet.getToInvite() + "', are they online?"));
                }
            case INVITE_SENT:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("Sent friend request to " + packet.getToInvite() + "!"));
                }
                if (invitedPlayer != null) {
                    invitedPlayer.sendMessage(new TextComponent("You have a new pending friend request from " + creator.name + "!"));
                }
            case ALREADY_INVITED:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("You already have a pending friend request to " + packet.getToInvite() + "!"));
                }
            case INVITE_LIMIT:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("You have hit the outgoing friend request limit!"));
                }
            case INVITE_NOT_ALLOWED:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("You cannot friend request this player!"));
                }
            default:
                assert false : "unknown field";
        }
    }

}