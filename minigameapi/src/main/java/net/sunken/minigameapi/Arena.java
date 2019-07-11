package net.sunken.minigameapi;

import com.google.common.base.Preconditions;
import net.sunken.minigameapi.event.ArenaStateChangeEvent;
import org.bukkit.Bukkit;

public class Arena {

    private ArenaState state;

    {
        state = ArenaState.UNDEFINED;
    }

    public ArenaState getState() {
        return state;
    }

    public void setState(ArenaState state) {
        Preconditions.checkState(
                state.ordinal() > this.state.ordinal(),
                "new arena state needs to be higher than the current");

        this.state = state;
        ArenaStateChangeEvent arenaStateChangeEvent = new ArenaStateChangeEvent(state);
        Bukkit.getPluginManager().callEvent(arenaStateChangeEvent);
    }
}
