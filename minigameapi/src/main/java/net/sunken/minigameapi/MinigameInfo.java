package net.sunken.minigameapi;

import lombok.Getter;

public class MinigameInfo {

    @Getter
    private final int minPlayers;
    @Getter
    private final int maxPlayers;

    public MinigameInfo(int minPlayers, int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }
}
