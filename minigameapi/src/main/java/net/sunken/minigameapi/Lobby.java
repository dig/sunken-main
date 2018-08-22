package net.sunken.minigameapi;

import lombok.Getter;
import org.bukkit.Location;

public class Lobby {

    @Getter
    private final Location pasteLocation;
    @Getter
    private final Location spawnLocation;

    public Lobby(Location pasteLocation, Location spawnLocation) {
        this.pasteLocation = pasteLocation;
        this.spawnLocation = spawnLocation;
    }
}
