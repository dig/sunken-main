package net.sunken.master;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.server.ServerCacheUpdater;
import net.sunken.master.parkour.ParkourCache;
import net.sunken.master.parkour.ParkourCacheUpdater;

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

    public void onEnable(){
        Common.getInstance().onCommonLoad(true);

        this.parkourCache = new ParkourCache();
        ParkourCacheUpdater parkourCacheUpdater = new ParkourCacheUpdater(Common.getInstance().getRedis().getConnection());
        parkourCacheUpdater.start();
    }

    public void onDisable(){
        Common.getInstance().onCommonDisable();
    }

    public static void main(String[] args) {
        new Master();
    }

}
