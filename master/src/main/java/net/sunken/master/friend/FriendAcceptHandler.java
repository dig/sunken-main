package net.sunken.master.friend;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.friend.data.FriendStatus;
import net.sunken.common.friend.packet.FriendAcceptStatusPacket;
import net.sunken.common.friend.packet.MFriendAcceptPacket;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.util.PlayerDetail;
import net.sunken.master.Master;
import net.sunken.master.player.MasterPlayer;

import java.util.Map;
import java.util.UUID;

public class FriendAcceptHandler extends PacketHandler<MFriendAcceptPacket> {

    private static DataManager dataManager;
    private static Map<UUID, AbstractPlayer> onlinePlayers;
    private static FriendManager friendManager;

    static {
        dataManager = Common.getInstance().getDataManager();
        onlinePlayers = Common.getInstance().getDataManager().getOnlinePlayers();
        friendManager = Master.getInstance().getFriendManager();
    }

    @Override
    public void onReceive(MFriendAcceptPacket packet) {
        UUID creator = packet.getCreator();
        String targetName = packet.getTarget();

        UUID target = dataManager.getNameToUUID().get(targetName.toLowerCase());
        MasterPlayer creatorPlayer = (MasterPlayer) onlinePlayers.get(creator);

        //--- Find status.
        FriendStatus status = FriendStatus.INVALID_PLAYER;
        if (target != null) {
            MasterPlayer targetPlayer = (MasterPlayer) onlinePlayers.get(target);
            boolean hasInvited = friendManager.getFriendInvites().containsEntry(target, creator);

            if (hasInvited) {
                friendManager.getFriendInvites().remove(target, creator);

                if (!packet.isDeny()) {
                    status = FriendStatus.PLAYER_ADDED;

                    //--- Add creator to targets friend list.
                    targetPlayer.addFriend(creatorPlayer.getPlayerDocument().getObjectId("_id"));

                    //--- Add target to creators friend list.
                    creatorPlayer.addFriend(targetPlayer.getPlayerDocument().getObjectId("_id"));

                } else {
                    status = FriendStatus.INVITE_DENY;
                }
            } else {
                status = FriendStatus.INVITE_NOT_FOUND;
            }
        }

        //--- Send packet back to bungeecords.
        PacketUtil.sendPacket(new FriendAcceptStatusPacket(
                new PlayerDetail(creator, creatorPlayer.getName()),
                packet.getTarget(), status));
    }
}
