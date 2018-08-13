package net.sunken.minigameapi.npc;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;

public class NPCListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        for(World w : Bukkit.getWorlds()){
            for(Entity ent : w.getEntities()){
                if(ent instanceof NPC){
                    NPC npc = (NPC) ent;

                    if(npc.isVisible()){
                        npc.showToPlayer(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();

        if(event.getRightClicked() instanceof NPC){
            NPC target = (NPC) event.getRightClicked();
            Bukkit.getPluginManager().callEvent(new NPCInteractEvent(player, target, event.getHand(), NPCInteractEvent.NPCInteractType.INTERACT));
        }
    }

    @EventHandler
    public void onPlayerInteractAt(PlayerInteractAtEntityEvent event){
        Player player = event.getPlayer();

        if(event.getRightClicked() instanceof NPC){
            NPC target = (NPC) event.getRightClicked();
            Bukkit.getPluginManager().callEvent(new NPCInteractEvent(player, target, event.getHand(), NPCInteractEvent.NPCInteractType.INTERACT_AT));
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof NPC){
            Player damager = (Player) event.getDamager();
            NPC target = (NPC) event.getEntity();
            Bukkit.getPluginManager().callEvent(new NPCInteractEvent(damager, target, EquipmentSlot.HAND, NPCInteractEvent.NPCInteractType.ATTACK));
        }
    }

}
