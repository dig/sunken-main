package net.sunken.master.player;

import net.sunken.common.Common;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.player.packet.PlayerConnectPacket;

import java.util.Map;
import java.util.UUID;

public class PlayerConnectHandler extends PacketHandler<PlayerConnectPacket> {

    @Override
    public void onReceive(PlayerConnectPacket packet) {
        Map<UUID, AbstractPlayer> players = Common.getInstance()
                .getDataManager()
                .getOnlinePlayers();

        if (players.containsKey(packet.getUuid())) {
            MasterPlayer masterPlayer = (MasterPlayer) players.get(packet.getUuid());
            masterPlayer.setServerName(packet.getServerName());
        }
    }
}
