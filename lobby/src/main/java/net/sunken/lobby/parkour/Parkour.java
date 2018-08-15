package net.sunken.lobby.parkour;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.lobby.player.LobbyPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Parkour {

    @Getter
    private String id;
    @Getter
    private HashMap<UUID, Long> players;

    @Getter
    private Material mainBlock;
    @Getter
    private ArrayList<Material> allowedMaterials;

    @Getter
    private Location startLocation;
    @Getter
    private Location endLocation;
    @Getter
    private List<Location> checkpoints;
    private Location resetPoint;

    private boolean timed;

    public Parkour (String id, Material mainBlock, ArrayList<Material> allowedMaterials,
                    Location start, Location end, List<Location> checkpoints,
                    Location resetPoint, boolean timed){
        this.id = id;
        this.players = new HashMap<>();

        this.mainBlock = mainBlock;
        this.allowedMaterials = allowedMaterials;

        this.startLocation = start;
        this.endLocation = end;
        this.checkpoints = checkpoints;
        this.resetPoint = resetPoint;

        this.timed = timed;
    }

    public void addPlayer(LobbyPlayer player){
        this.players.put(player.getUUID(), System.currentTimeMillis());
        player.toPlayer().sendMessage("starting parkour");
    }

    public void stopPlayer(LobbyPlayer player, boolean finished){
        if(finished){
            long time = System.currentTimeMillis() - this.players.get(player.getUUID());
            player.updateParkourTime(this.id, time);
            player.toPlayer().sendMessage("end");
        }

        players.remove(player.getUUID());
        player.toPlayer().teleport(this.resetPoint);
        player.toPlayer().sendMessage("finished parkour");
    }

    public void setCheckpoint(LobbyPlayer player, int index){
        player.toPlayer().sendMessage("checkpoint " + index);
    }

}
