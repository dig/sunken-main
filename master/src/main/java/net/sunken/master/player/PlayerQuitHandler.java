package net.sunken.master.player;

import net.sunken.common.Common;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.player.packet.PlayerQuitPacket;

public class PlayerQuitHandler extends PacketHandler<PlayerQuitPacket> {

    @Override
    public void onReceive(PlayerQuitPacket packet) {
        Common.getInstance()
                .getDataManager()
                .getOnlinePlayers()
                .remove(packet.getUuid());
    }

}
