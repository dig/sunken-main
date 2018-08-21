package net.sunken.minigameapi;

import org.bukkit.Bukkit;

public class Arena {

    private ArenaState state;

    {
        state = ArenaState.LOBBY;
    }

    public ArenaState getState() {
        return state;
    }

    public void setState(ArenaState state) {
        this.state = state;
        ArenaStateChangeEvent arenaStateChangeEvent = new ArenaStateChangeEvent(state);
        Bukkit.getPluginManager().callEvent(arenaStateChangeEvent);
    }
}
