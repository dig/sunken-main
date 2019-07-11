package net.sunken.master.parkour;

import net.sunken.common.Common;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parkour.ParkourCacheUpdatePacket;
import net.sunken.common.parkour.ParkourLeaderboardUpdatePacket;
import net.sunken.master.Master;

import java.util.logging.Level;

public class ParkourCacheHandler extends PacketHandler<ParkourCacheUpdatePacket> {

    @Override
    public void onReceive(ParkourCacheUpdatePacket packet) {
        Common.getLogger().log(Level.INFO, "Updating parkour (" + packet.getId() + ") cache due to request");

        Master.getInstance().getParkourCache().updateCache(packet.getId()).thenRun(() -> {
            PacketUtil.sendPacket(new ParkourLeaderboardUpdatePacket());
        });
    }
}