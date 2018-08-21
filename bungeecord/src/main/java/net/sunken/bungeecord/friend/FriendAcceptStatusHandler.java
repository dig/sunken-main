package net.sunken.bungeecord.friend;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.friend.packet.FriendAcceptStatusPacket;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.util.PlayerDetail;

public class FriendAcceptStatusHandler extends PacketHandler<FriendAcceptStatusPacket> {

    @Override
    public void onReceive(FriendAcceptStatusPacket packet) {
        PlayerDetail creator = packet.getCreator();

        ProxiedPlayer creatorPlayer = ProxyServer.getInstance().getPlayer(creator.uuid);
        ProxiedPlayer invitedPlayer = ProxyServer.getInstance().getPlayer(packet.getTarget());

        switch (packet.getStatus()) {
            case INVITE_NOT_FOUND:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("Could not find a friend request from '" + packet.getTarget() + "'."));
                }
            case INVITE_DENY:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("You have denied " + packet.getTarget() + " friend request."));
                }
                if (invitedPlayer != null) {
                    invitedPlayer.sendMessage(new TextComponent(creator.name + " has denied your friend request."));
                }
            case PLAYER_ADDED:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("You are now friends with " + packet.getTarget() + "!"));
                }
                if (invitedPlayer != null) {
                    invitedPlayer.sendMessage(new TextComponent("You are now friends with " + creator.name + "!"));
                }
            default:
                assert false : "unknown field";
        }
    }
}
