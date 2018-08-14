package net.sunken.lobby.listeners;

import com.mojang.datafixers.types.templates.Const;
import net.sunken.core.util.ScoreboardUtil;
import net.sunken.core.util.TabListUtil;
import net.sunken.core.util.chat.MessageUtil;
import net.sunken.lobby.Constants;
import net.sunken.lobby.LobbyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("");

        FileConfiguration config = LobbyPlugin.getInstance().getConfig();

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
}