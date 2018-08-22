package net.sunken.minigameapi;

public class LobbyPoller implements Runnable {

    private final MinigameBase minigame;

    public LobbyPoller(MinigameBase minigame) {
        this.minigame = minigame;
    }

    @Override
    public void run() {
        ArenaState arenaState = minigame.getArena().getState();
        if (arenaState == ArenaState.LOBBY) {
            // TODO: countdowns based on player count etc.
        }
    }
}
