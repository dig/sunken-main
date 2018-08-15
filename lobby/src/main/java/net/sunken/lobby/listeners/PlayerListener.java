package net.sunken.lobby.listeners;

import net.sunken.common.Common;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.core.util.TabListUtil;
import net.sunken.core.util.chat.MessageUtil;
import net.sunken.lobby.Constants;
import net.sunken.lobby.LobbyPlugin;
import net.sunken.lobby.player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = LobbyPlugin.getInstance().getConfig();
        event.setJoinMessage("");

        // Add to hashmap
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);
        Common.getInstance().getOnlinePlayers().put(player.getUniqueId().toString(), lobbyPlayer);

        // Set spawn
        Location spawn = new Location(
                Bukkit.getWorld(config.getString("spawn.world")),
                config.getDouble("spawn.x"),
                config.getDouble("spawn.y"),
                config.getDouble("spawn.z"),
                config.getInt("spawn.yaw"),
                config.getInt("spawn.pitch")
        );
        player.teleport(spawn);

        // Reset player
        player.getInventory().clear();
        player.getActivePotionEffects().clear();
        player.setGameMode(GameMode.SURVIVAL);

        // Tab list
        TabListUtil.sendTabTitle(player, Constants.TAB_TOP, Constants.TAB_BOTTOM);

        // Chat
        for(int i = 0; i<Constants.JOIN_MESSAGES.size(); i++){
            player.sendMessage(MessageUtil.getCenteredMessage(Constants.JOIN_MESSAGES.get(i), MessageUtil.CENTER_PX));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        event.setQuitMessage("");

        ConcurrentHashMap<String, AbstractPlayer> players = Common.getInstance().getOnlinePlayers();
        players.get(player.getUniqueId().toString()).cleanup();
        players.remove(player.getUniqueId().toString());
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        event.setCancelled(true);
        event.setFoodLevel(15);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        Player player = event.getPlayer();

        if(player.getGameMode() != GameMode.CREATIVE){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(final EntityDamageEvent event) {
        Entity e = event.getEntity();

        if(e instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(player.getGameMode() != GameMode.CREATIVE){
            event.setCancelled(true);
        }
    }
}