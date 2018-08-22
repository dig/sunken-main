package net.sunken.minigameapi;

import lombok.Getter;
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
