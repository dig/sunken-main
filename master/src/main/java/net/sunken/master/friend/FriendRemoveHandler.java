package net.sunken.master.friend;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.friend.data.FriendStatus;
import net.sunken.common.friend.packet.FriendStatusPacket;
import net.sunken.common.friend.packet.MFriendRemovePacket;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.player.PlayerRank;
import net.sunken.common.util.PlayerDetail;
import net.sunken.master.Master;
import net.sunken.master.player.MasterPlayer;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;

public class FriendRemoveHandler extends PacketHandler<MFriendRemovePacket> {

    private static DataManager dataManager;
    private static Map<UUID, AbstractPlayer> onlinePlayers;
    private static FriendManager friendManager;

    static {
        dataManager = Common.getInstance().getDataManager();
        onlinePlayers = Common.getInstance().getDataManager().getOnlinePlayers();
        friendManager = Master.getInstance().getFriendManager();
    }

    @Override
    public void onReceive(MFriendRemovePacket packet) {
        UUID creator = packet.getCreator();
        MasterPlayer creatorPlayer = (MasterPlayer) onlinePlayers.get(creator);
        String targetName = packet.getTarget();

        FriendStatus status = FriendStatus.NOT_FRIENDS;
        creatorPlayer.loadFriendsCache();

        for (Document friend : creatorPlayer.getFriends()) {
            String name = friend.getString(DatabaseConstants.PLAYER_NAME_FIELD);
            PlayerRank rank = PlayerRank.valueOf(friend.getString(DatabaseConstants.PLAYER_RANK_FIELD));

            if (name.equalsIgnoreCase(targetName)) {

            }
        }

        PacketUtil.sendPacket(new FriendStatusPacket(
                new PlayerDetail(creator, creatorPlayer.getName()),
                packet.getTarget(), status));
    }

}
