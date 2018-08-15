package net.sunken.lobby.parkour;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.lobby.player.LobbyPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
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
    private HashMap<UUID, Integer> checkpoint;

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
        this.checkpoint = new HashMap<>();

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
        this.checkpoint.put(player.getUUID(), 0);

        player.sendMessage("&aParkour started! Finish the course in as little time as possible.");
    }

    public void stopPlayer(LobbyPlayer player, boolean finished){
        if(finished){
            long time = System.currentTimeMillis() - this.players.get(player.getUUID());

            DecimalFormat df = new DecimalFormat("0.000");
            player.sendMessage("&aParkour finished! You finished in " + df.format(((double) time / 1000)) + " seconds.");

            if(time < player.getBestTime(this.id)){
                player.updateParkourTime(this.id, time);
                player.sendMessage("&a&lNew personal best!");
            }
        }

        checkpoint.remove(player.getUUID());
        players.remove(player.getUUID());
        player.toPlayer().teleport(this.resetPoint);
    }

    public void setCheckpoint(LobbyPlayer player, int index){
        long time = System.currentTimeMillis() - this.players.get(player.getUUID());

        if((index + 1) > checkpoint.get(player.getUUID())){
            checkpoint.put(player.getUUID(), (index + 1));

            DecimalFormat df = new DecimalFormat("0.000");
            player.sendMessage("&aYou reached Checkpoint #" + (index + 1) + " in " + df.format(((double) time / 1000)) + " seconds.");
        }
    }

}
