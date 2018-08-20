package net.sunken.lobby.parkour;

import net.sunken.common.packet.PacketHandler;
import net.sunken.common.parkour.ParkourLeaderboardUpdatePacket;
import net.sunken.lobby.LobbyPlugin;

public class ParkourHandler extends PacketHandler<ParkourLeaderboardUpdatePacket> {

    @Override
    public void onReceive(ParkourLeaderboardUpdatePacket packet) {
        LobbyPlugin.getInstance().getParkourCache().updateBestTimes();
    }
}
