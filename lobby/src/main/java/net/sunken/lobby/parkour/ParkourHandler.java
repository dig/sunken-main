package net.sunken.lobby.parkour;

import net.sunken.common.Common;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.packets.ParkourLeaderboardUpdatePacket;
import net.sunken.lobby.LobbyPlugin;

import java.util.logging.Level;

public class ParkourHandler extends PacketHandler<ParkourLeaderboardUpdatePacket> {

    @Override
    public void onReceive(ParkourLeaderboardUpdatePacket packet) {
        LobbyPlugin.getInstance().getParkourCache().updateBestTimes();
    }
}
