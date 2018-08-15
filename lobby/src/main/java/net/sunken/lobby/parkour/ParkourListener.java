package net.sunken.lobby.parkour;

import net.sunken.common.Common;
import net.sunken.lobby.LobbyPlugin;
import net.sunken.lobby.player.LobbyPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class ParkourListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = (LobbyPlayer) Common.getInstance().getOnlinePlayers().get(player.getUniqueId().toString());

        Block block = player.getLocation().clone().subtract(0, 1, 0).getBlock();
        ParkourCache cache = LobbyPlugin.getInstance().getParkourCache();

        if(!block.getType().equals(Material.AIR)){
            Parkour current = cache.getCurrentParkour(lobbyPlayer);

            if(!(current == null) && !(current.getAllowedMaterials().contains(block.getType()))){
                current.stopPlayer(lobbyPlayer, false);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = (LobbyPlayer) Common.getInstance().getOnlinePlayers().get(player.getUniqueId().toString());

        Block block = event.getClickedBlock();
        ParkourCache cache = LobbyPlugin.getInstance().getParkourCache();

        if(event.getAction().equals(Action.PHYSICAL)){
            Parkour current = cache.getCurrentParkour(lobbyPlayer);

            if(current == null){
                for(Parkour parkour : cache.getParkours()){
                    if(this.compareLocation(parkour.getStartLocation(), block.getLocation())){
                        parkour.addPlayer(lobbyPlayer);
                    }
                }
            } else {
                if(this.compareLocation(current.getEndLocation(), block.getLocation())){
                    current.stopPlayer(lobbyPlayer, true);
                } else {
                    int i = 0;
                    for(Location loc : current.getCheckpoints()){
                        if(this.compareLocation(loc, block.getLocation())){
                            current.setCheckpoint(lobbyPlayer, i);
                        }
                        i++;
                    }
                }
            }
        }
    }

    private boolean compareLocation(Location loc, Location loc1){
        if(loc.getBlockX() == loc1.getBlockX() && loc.getBlockY() == loc1.getBlockY()
                && loc.getBlockZ() == loc1.getBlockZ()){
            return true;
        }
        return false;
    }


}
