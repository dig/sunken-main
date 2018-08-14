package net.sunken.lobby.player;

import net.sunken.common.player.AbstractPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LobbyPlayer extends AbstractPlayer {

    public LobbyPlayer(Player player){
        super(
                player.getUniqueId().toString(),
                player.getName()
        );
    }

    public Player toPlayer(){
        return Bukkit.getPlayer(this.getUUID());
    }

}
