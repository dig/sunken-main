package net.sunken.master.player;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.player.packet.PlayerJoinPacket;

public class PlayerJoinHandler extends PacketHandler<PlayerJoinPacket> {

    @Override
    public void onReceive(PlayerJoinPacket packet) {
        MasterPlayer masterPlayer = new MasterPlayer(packet.getUuid().toString(), packet.getName(),
                packet.getDocument(), packet.isFirstJoin());

        DataManager dataManager = Common.getInstance().getDataManager();
        dataManager.getOnlinePlayers()
                .put(packet.getUuid(), masterPlayer);
        dataManager.getNameToUUID()
                .put(packet.getName().toLowerCase(), packet.getUuid());
    }

}