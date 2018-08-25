package net.sunken.master.friend;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.friend.data.FriendStatus;
import net.sunken.common.friend.packet.FriendAcceptPacket;
import net.sunken.common.friend.packet.FriendAcceptStatusPacket;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.util.PlayerDetail;
import net.sunken.master.Master;
import net.sunken.master.player.MasterPlayer;

import java.util.Map;
import java.util.UUID;

public class FriendAcceptHandler extends PacketHandler<FriendAcceptPacket> {

    private static DataManager dataManager;
    private static Map<UUID, AbstractPlayer> onlinePlayers;
    private static FriendManager friendManager;

    static {
        dataManager = Common.getInstance().getDataManager();
        onlinePlayers = Common.getInstance().getDataManager().getOnlinePlayers();
        friendManager = Master.getInstance().getFriendManager();
    }

    @Override
    public void onReceive(FriendAcceptPacket packet) {
        UUID creator = packet.getCreator();
        String targetName = packet.getTarget();

        UUID target = dataManager.getNameToUUID().get(targetName.toLowerCase());
        String creatorName = onlinePlayers.get(creator).getName();

        FriendStatus status = FriendStatus.INVALID_PLAYER;
        if (target != null) {
            boolean hasInvited = friendManager.getFriendInvites().containsEntry(target, creator);

            if (hasInvited) {
                friendManager.getFriendInvites().remove(target, creator);

                if (!packet.isDeny()) {
                    MasterPlayer creatorPlayer = (MasterPlayer) onlinePlayers.get(creator);
                    MasterPlayer targetPlayer = (MasterPlayer) onlinePlayers.get(target);

                    creatorPlayer.addFriend(targetPlayer.getPlayerDocument().getObjectId("_id"));
                    targetPlayer.addFriend(creatorPlayer.getPlayerDocument().getObjectId("_id"));

                    status = FriendStatus.PLAYER_ADDED;
                } else {
                    status = FriendStatus.INVITE_DENY;
                }
            } else {
                status = FriendStatus.INVITE_NOT_FOUND;
            }
        }

        PacketUtil.sendPacket(new FriendAcceptStatusPacket(
                new PlayerDetail(creator, creatorName),
                packet.getTarget(), status));
    }
}
