package me.minigames.lobby;

import me.minigames.Minigame;
import me.minigames.MinigamePlugin;
import me.minigames.utils.NametagUtil;
import me.minigames.utils.TabListUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

import static me.minigames.MinigameState.LOBBY;
import static org.bukkit.event.block.Action.LEFT_CLICK_BLOCK;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class LobbyListener implements Listener {
    private Minigame minigame = MinigamePlugin.getInstance().getMinigame();

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Server server = Bukkit.getServer();
        int onlinePlayers = server.getOnlinePlayers().size();
        int maxPlayers = MinigamePlugin.getInstance().getMinigame().getMaxPlayers();

        if (onlinePlayers >= maxPlayers) {
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, "The game server you attempted to join is full, try another");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (LobbyManager.getScoreboard() == null) {
            LobbyManager.setupScoreboard();
        }

        LobbyManager.getScoreboard().send(player);
        NametagUtil.changePlayerName(player, "&7", "", NametagUtil.TeamAction.CREATE);
        TabListUtil.sendTabTitle(player, "title", "subtitle");

        player.teleport(new Location(Bukkit.getWorld("world"), 0, 90, 0));
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setFormat(ChatColor.GRAY + player.getName() + ": " + ChatColor.WHITE + event.getMessage());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (minigame.getState() == LOBBY) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (minigame.getState() == LOBBY) {
            if (e.getEntity() instanceof Player) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDecay(LeavesDecayEvent e) {
        if (minigame.getState() == LOBBY) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (minigame.getState() == LOBBY) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (minigame.getState() == LOBBY) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (minigame.getState() == LOBBY) {
            Action action = e.getAction();
            if (action == RIGHT_CLICK_BLOCK || action == LEFT_CLICK_BLOCK) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSwapHandItems(PlayerSwapHandItemsEvent e) {
        if (minigame.getState() == LOBBY) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent e) {
        if (minigame.getState() == LOBBY) {
            e.setCancelled(true);
        }
    }
}
