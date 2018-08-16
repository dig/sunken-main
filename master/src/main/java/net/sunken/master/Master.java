package net.sunken.master;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.packet.PacketHandlerRegistry;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.packet.packets.ParkourCacheUpdatePacket;
import net.sunken.common.packet.packets.ParkourLeaderboardUpdatePacket;
import net.sunken.master.parkour.ParkourCache;
import net.sunken.master.parkour.ParkourCacheHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Master {

    @Getter
    private static Master instance;

    @Getter
    private ParkourCache parkourCache;

    public Master() {
        instance = this;
        this.onEnable();

        // Keep application running
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onEnable(){
        Common.getInstance().onCommonLoad(true);

        // Register packets
        PacketHandlerRegistry.registerHandler(new ParkourCacheUpdatePacket(), new ParkourCacheHandler());

        this.parkourCache = new ParkourCache();

        Master.getInstance().getParkourCache().updateCache("test").thenRun(() -> {
            PacketUtil.sendPacket(new ParkourLeaderboardUpdatePacket());
        });
    }

    public void onDisable(){
        Common.getInstance().onCommonDisable();
    }

    public static void main(String[] args) {
        new Master();
    }
}
