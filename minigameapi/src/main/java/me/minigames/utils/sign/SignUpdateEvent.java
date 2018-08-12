package me.minigames.utils.sign;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Digital on 22/01/2018.
 */
public class SignUpdateEvent extends Event {

    @Getter
    private Player player;

    @Getter
    private String[] lines;

    public SignUpdateEvent(Player player, String[] lines){
        this.player = player;
        this.lines = lines;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
