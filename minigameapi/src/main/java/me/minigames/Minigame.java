package me.minigames;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import me.minigames.player.AbstractPlayer;

import java.util.ArrayList;
import java.util.List;

public class Minigame {

    @Getter
    private final String name;
    @Getter
    private final int maxPlayers;
    @Getter
    private final int requiredPlayers;
    @Getter
    private final boolean voidWorld;
    @Getter
    @Setter
    private long time;
    @Getter
    private long maxTime;
    @Getter
    private long minTime;

    @Getter
    private List<AbstractPlayer> players;
    @Getter
    public MinigameState state;

    public Minigame(String name, int maxPlayers, int requiredPlayers, boolean voidWorld, long maxTime, long minTime) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.requiredPlayers = requiredPlayers;
        this.voidWorld = voidWorld;
        this.time = 6000;

        this.maxTime = maxTime;
        this.minTime = minTime;

        this.players = new ArrayList<AbstractPlayer>();
        this.state = MinigameState.LOBBY;
    }

    public void setState(MinigameState state) {
        Preconditions.checkState(state != MinigameState.LOBBY, "Cannot set lobby state after game has loaded");
        this.state = state;
    }
}
