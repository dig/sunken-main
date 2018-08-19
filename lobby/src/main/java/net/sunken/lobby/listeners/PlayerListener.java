package net.sunken.lobby.listeners;

import net.sunken.common.Common;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.service.PartyService;
import net.sunken.common.parties.service.RedisPartyService;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.player.PlayerRank;
import net.sunken.common.server.ServerObject;
import net.sunken.common.type.ServerType;
import net.sunken.core.inventory.ItemBuilder;
import net.sunken.core.inventory.element.Action;
import net.sunken.core.inventory.element.ActionableElement;
import net.sunken.core.util.NametagUtil;
import net.sunken.core.util.TabListUtil;
import net.sunken.core.util.chat.MessageUtil;
import net.sunken.lobby.Constants;
import net.sunken.lobby.LobbyPlugin;
import net.sunken.lobby.parkour.ParkourData;
import net.sunken.lobby.player.LobbyPlayer;
import org.bukkit.*;
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
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class PlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = LobbyPlugin.getInstance().getConfig();
        event.setJoinMessage("");

        // Add to onlinePlayers
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

        // Reset
        player.getInventory().clear();
        player.getActivePotionEffects().clear();
        player.setGameMode(GameMode.ADVENTURE);

        // Tab
        TabListUtil.sendTabTitle(player, Constants.TAB_TOP, Constants.TAB_BOTTOM);

        // Chat
        for(int i = 0; i<Constants.JOIN_MESSAGES.size(); i++){
            player.sendMessage(MessageUtil.getCenteredMessage(Constants.JOIN_MESSAGES.get(i), MessageUtil.CENTER_PX));
        }

        // Rank
        player.setPlayerListName(lobbyPlayer.getRankColour() + player.getName());
        NametagUtil.changePlayerName(player, lobbyPlayer.getRankColour(), NametagUtil.TeamAction.CREATE);

        // Inventory
        player.getInventory().setItem(0, new ActionableElement(Constants.ITEM_SELECTOR.make(), Action.INTERACT, context -> {
            Player observer = context.getObserver();
            observer.sendMessage("selector");
            return context;
        }).getItem());

        player.getInventory().setItem(8, new ActionableElement(Constants.ITEM_LOBBY.make(), Action.INTERACT, context -> {
            Player observer = context.getObserver();
            observer.openInventory(LobbyPlugin.getInstance().getLobbyInventory());
            return context;
        }).getItem());
    }

    @EventHandler(ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        event.setQuitMessage("");

        // Remove team created for nametag colour
        NametagUtil.changePlayerName(player, ChatColor.GRAY, NametagUtil.TeamAction.DESTROY);

        // Remove player once they are gone
        Map<String, AbstractPlayer> players = Common.getInstance().getOnlinePlayers();
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
}