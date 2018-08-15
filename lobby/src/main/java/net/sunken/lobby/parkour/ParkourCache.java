package net.sunken.lobby.parkour;

import lombok.Getter;
import net.sunken.lobby.player.LobbyPlayer;
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

        this.loadParkours();
    }

    private void loadParkours(){

        
    }

    public boolean inParkour(LobbyPlayer player){
        for(Parkour parkour : this.parkours){
            if(parkour.getPlayers().containsKey(player.getUUID())){
                return true;
            }
        }

        return false;
    }

}
