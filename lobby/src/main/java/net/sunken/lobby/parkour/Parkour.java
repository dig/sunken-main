package net.sunken.lobby.parkour;

import lombok.Getter;
import net.sunken.lobby.player.LobbyPlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Parkour {

    @Getter
    private HashMap<UUID, Double> players;

    private Block start;
    private Block end;
    private List<Block> checkpoints;

    private boolean teleportCheckpoint;
    private boolean timed;

    public Parkour (Block start, Block end, List<Block> checkpoints, boolean teleportCheckpoint, boolean timed){
        this.players = new HashMap<UUID, Double>();
        this.start = start;
        this.end = end;
        this.checkpoints = checkpoints;
        this.teleportCheckpoint = teleportCheckpoint;
        this.timed = timed;
    }

    public void addPlayer(LobbyPlayer player){

    }

}
