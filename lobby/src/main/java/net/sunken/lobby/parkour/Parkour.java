package net.sunken.lobby.parkour;

import lombok.Getter;
import net.sunken.lobby.player.LobbyPlayer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Parkour {

    @Getter
    private String id;
    @Getter
    private HashMap<UUID, Long> players;

    private Block start;
    private Block end;
    private List<Block> checkpoints;
    private Location resetPoint;

    private boolean timed;

    public Parkour (String id, Block start, Block end,
                    List<Block> checkpoints, Location resetPoint, boolean timed){
        this.id = id;
        this.players = new HashMap<>();

        this.start = start;
        this.end = end;
        this.checkpoints = checkpoints;
        this.resetPoint = resetPoint;

        this.timed = timed;
    }

    public void addPlayer(LobbyPlayer player){
        this.players.put(player.getUUID(), System.currentTimeMillis());
    }

    public void stopPlayer(LobbyPlayer player, boolean finished){
        if(finished){
            long time = System.currentTimeMillis() - this.players.get(player.getUUID());
            player.updateParkourTime(this.id, time);
        }

        players.remove(player.getUUID());
        player.toPlayer().teleport(this.resetPoint);
    }

}
