package net.sunken.master.player;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.player.packet.PlayerQuitPacket;

public class PlayerQuitHandler extends PacketHandler<PlayerQuitPacket> {

    @Override
    public void onReceive(PlayerQuitPacket packet) {
        DataManager dataManager = Common.getInstance().getDataManager();
        dataManager.getOnlinePlayers()
                .remove(packet.getUuid());
        dataManager.getNameToUUID()
                .remove(packet.getName().toLowerCase());
    }

}
