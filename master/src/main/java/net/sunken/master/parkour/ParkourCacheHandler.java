package net.sunken.master.parkour;

import net.sunken.common.Common;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.packets.ParkourCacheUpdatePacket;
import net.sunken.master.Master;

public class ParkourCacheHandler extends PacketHandler<ParkourCacheUpdatePacket> {

    @Override
    public void onReceive(ParkourCacheUpdatePacket packet) {
        Master.getInstance().getParkourCache().updateCache(packet.getId()).thenRun(() -> {
            Common.getInstance().getRedis().sendRedisMessage(ParkourRedisHelper.PARKOUR_CACHE_CHANNEL,
                                                             ParkourRedisHelper.UPDATE_PARKOUR_LEADERBOARD);
        });
    }
}