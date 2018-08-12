package me.minigames.lobby;

import lombok.Getter;
import me.minigames.MinigamePlugin;
import me.minigames.utils.ScoreboardUtil;
import me.minigames.utils.world.SchematicUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class LobbyManager {
    private LobbyManager() {}

    @Getter
    private static ScoreboardUtil scoreboard;

    private static List<Block> lobbyBlocks = new ArrayList<>();

    public static void loadLobbySchematic() {
        String root = Bukkit.getServer().getWorldContainer().getAbsolutePath();
        // Removed root dir from schematic path, may work
        String schematic = File.separator + "schematics" + File.separator + "lobby.schematic";

        try {
            lobbyBlocks = SchematicUtil.loadAndPaste(schematic, Bukkit.getWorld("world"), new Location(Bukkit.getWorld("world"), 0, 90, 0));
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error occurred pasting lobby");
            Bukkit.shutdown();
        }
    }

    public static void removeLobbySchematic() {
        for (Block b : lobbyBlocks) {
            b.setType(Material.AIR);
        }
    }

    public static void setupScoreboard(){
        scoreboard = new ScoreboardUtil("&c" + MinigamePlugin.getInstance().getMinigame().getName());
        scoreboard.add("test" , 1);
        scoreboard.update();
    }
}
