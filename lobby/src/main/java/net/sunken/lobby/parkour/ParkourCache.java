package net.sunken.lobby.parkour;

import lombok.Getter;
import org.bukkit.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

public class ParkourCache {

    @Getter
    private List<Parkour> parkours;
    private Configuration config;

    public ParkourCache(Configuration config){
        this.parkours = new ArrayList<Parkour>();
        this.config = config;
    }

    private void loadParkours(){

    }

}
