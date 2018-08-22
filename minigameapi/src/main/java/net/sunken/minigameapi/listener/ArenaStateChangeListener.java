package net.sunken.minigameapi.listener;

import net.sunken.minigameapi.ArenaState;
import net.sunken.minigameapi.event.ArenaStateChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaStateChangeListener implements Listener {

    @EventHandler
    public void onStateChange(ArenaStateChangeEvent e) {
        ArenaState toState = e.getToState();
        switch (toState) {
            case LOBBY:
                // TODO: spawn in lobby schematic
                break;
            case IN_GAME:
                // TODO: get rid of lobby schematic
                break;
            case ENDED:
                // TODO: kick players to hub/minigame lobby
                break;
            default:
        }
    }
}
