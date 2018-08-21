package net.sunken.master.player;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.player.packet.PlayerQuitPacket;
import net.sunken.master.Master;
import net.sunken.master.friend.FriendManager;

public class PlayerQuitHandler extends PacketHandler<PlayerQuitPacket> {

    @Override
    public void onReceive(PlayerQuitPacket packet) {
        // Remove from player cache
        DataManager dataManager = Common.getInstance().getDataManager();
        dataManager.getOnlinePlayers()
                .remove(packet.getUuid());
        dataManager.getNameToUUID()
                .remove(packet.getName().toLowerCase());

        // Remove pending friend invites
        FriendManager friendManager = Master.getInstance().getFriendManager();
        friendManager.getFriendInvites().removeAll(packet.getUuid());
    }

}
