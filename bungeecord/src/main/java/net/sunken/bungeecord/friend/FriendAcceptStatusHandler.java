package net.sunken.bungeecord.friend;

import com.sk89q.minecraft.util.commands.Command;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.friend.packet.FriendAcceptStatusPacket;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.util.PlayerDetail;

public class FriendAcceptStatusHandler extends PacketHandler<FriendAcceptStatusPacket> {

    private static DataManager dataManager;

    static {
        dataManager = Common.getInstance().getDataManager();
    }

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
                break;
            case INVITE_DENY:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("You have denied " + packet.getTarget() + " friend request."));
                }
                if (invitedPlayer != null) {
                    invitedPlayer.sendMessage(new TextComponent(creator.name + " has denied your friend request."));
                }
                break;
            case PLAYER_ADDED:
                if (creatorPlayer != null) {
                    creatorPlayer.sendMessage(new TextComponent("You are now friends with " + packet.getTarget() + "!"));
                }
                if (invitedPlayer != null) {
                    invitedPlayer.sendMessage(new TextComponent("You are now friends with " + creator.name + "!"));
                }
                break;
            default:
                assert false : "unknown field";
        }
    }
}
