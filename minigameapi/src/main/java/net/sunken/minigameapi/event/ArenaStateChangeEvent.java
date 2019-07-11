package net.sunken.minigameapi.event;

import lombok.Getter;
import net.sunken.minigameapi.ArenaState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaStateChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final ArenaState toState;

    public ArenaStateChangeEvent(ArenaState toState) {
        this.toState = toState;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
