package net.sunken.master.player;

import net.sunken.common.Common;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.player.packet.PlayerConnectPacket;

public class PlayerConnectHandler extends PacketHandler<PlayerConnectPacket> {

    @Override
    public void onReceive(PlayerConnectPacket packet) {
        MasterPlayer masterPlayer = (MasterPlayer) Common.getInstance()
                .getDataManager()
                .getOnlinePlayers()
                .get(packet.getUuid());
        masterPlayer.setServerName(packet.getServerName());
    }
}
