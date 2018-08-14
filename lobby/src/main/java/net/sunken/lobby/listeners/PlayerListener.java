package net.sunken.lobby.listeners;

import net.sunken.common.Common;
import net.sunken.core.util.TabListUtil;
import net.sunken.core.util.chat.MessageUtil;
import net.sunken.lobby.Constants;
import net.sunken.lobby.LobbyPlugin;
import net.sunken.lobby.player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
                config.getDouble("spawn.z")
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

        Common.getInstance().getOnlinePlayers().remove(player.getUniqueId().toString());
    }
}