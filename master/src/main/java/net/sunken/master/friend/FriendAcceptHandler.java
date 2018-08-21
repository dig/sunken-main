package net.sunken.master.friend;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.friend.FriendAcceptPacket;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.master.Master;

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
    }

}
