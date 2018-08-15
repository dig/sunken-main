package net.sunken.lobby.parkour;

import lombok.Getter;
import net.minecraft.server.v1_13_R1.WorldGenVillagePieces;
import net.sunken.common.Common;
import net.sunken.lobby.player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
        ConfigurationSection all = this.config.getConfigurationSection("parkours");

        for(String id : all.getKeys(false)){
            ConfigurationSection parkour = this.config.getConfigurationSection("parkours." + id);

            Material mainBlock = Material.valueOf(parkour.getString("mainMaterial"));
            Location start = this.parseCoordinates(parkour.getConfigurationSection("start"));
            Location end = this.parseCoordinates(parkour.getConfigurationSection("end"));
            Location resetPoint = this.parseCoordinates(parkour.getConfigurationSection("resetPoint"));

            List<Location> checkpoints = new ArrayList<Location>();
            for(String index : parkour.getConfigurationSection("checkpoints").getKeys(false)){
                checkpoints.add(this.parseCoordinates(parkour.getConfigurationSection("checkpoints." + index)));
            }

            ArrayList<Material> allowedMaterials = new ArrayList<Material>();
            for(String matName : parkour.getStringList("allowedMaterials")){
                allowedMaterials.add(Material.valueOf(matName));
            }

            parkours.add(new Parkour(id, mainBlock, allowedMaterials, start, end, checkpoints, resetPoint, true));
        }
    }

    private Location parseCoordinates(ConfigurationSection section){
        String worldName = Bukkit.getWorlds().get(0).getName();
        if(section.contains("world")){
            worldName = section.getString("world");
        }

        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");

        double yaw = 0;
        if(section.contains("yaw")){
            yaw = section.getDouble("yaw");
        }

        double pitch = 0;
        if(section.contains("pitch")){
            pitch = section.getDouble("pitch");
        }

        return new Location(Bukkit.getWorld(worldName), x, y, z, (float) yaw, (float) pitch);
    }

    public Parkour getCurrentParkour(LobbyPlayer player){
        for(Parkour parkour : this.parkours){
            if(parkour.getPlayers().containsKey(player.getUUID())){
                return parkour;
            }
        }

        return null;
    }

}
