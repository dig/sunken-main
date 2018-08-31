package net.sunken.master.friend;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.friend.data.FriendStatus;
import net.sunken.common.friend.packet.FriendStatusPacket;
import net.sunken.common.friend.packet.MFriendRequestPacket;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.util.PlayerDetail;
import net.sunken.master.Master;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class FriendRequestHandler extends PacketHandler<MFriendRequestPacket> {

    private static DataManager dataManager;
    private static Map<UUID, AbstractPlayer> onlinePlayers;
    private static FriendManager friendManager;

    static {
        dataManager = Common.getInstance().getDataManager();
        onlinePlayers = Common.getInstance().getDataManager().getOnlinePlayers();
        friendManager = Master.getInstance().getFriendManager();
    }

    @Override
    public void onReceive(MFriendRequestPacket packet) {
        UUID creator = packet.getCreator();
        String toInviteName = packet.getToInvite();

        String creatorName = onlinePlayers.get(creator).getName();
        UUID toInvite = dataManager.getNameToUUID().get(toInviteName.toLowerCase());

        FriendStatus status = FriendStatus.INVALID_PLAYER;
        if (toInvite != null) {
            boolean needsToAccept = friendManager.getFriendInvites().containsEntry(toInvite, creator);
            boolean alreadyInvited = friendManager.getFriendInvites().containsEntry(creator, toInvite);

            if (!alreadyInvited && !needsToAccept) {
                int totalInvites = 0;

                Collection<UUID> creatorInvites = friendManager.getFriendInvites().get(creator);
                if (creatorInvites != null) {
                    totalInvites = creatorInvites.size();
                }

                if (totalInvites < 10) {
                    friendManager.getFriendInvites().put(creator, toInvite);
                    status = FriendStatus.INVITE_SENT;
                } else {
                    status = FriendStatus.INVITE_LIMIT;
                }
            } else if (needsToAccept) {
                status = FriendStatus.INVITE_PENDING;
            } else {
                status = FriendStatus.ALREADY_INVITED;
            }
        }

        PacketUtil.sendPacket(new FriendStatusPacket(
                new PlayerDetail(creator, creatorName),
                packet.getToInvite(), status));
    }
}
