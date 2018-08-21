package net.sunken.common.player;

import lombok.Getter;

public enum PlayerRank {

    USER(0, "GRAY", "USER"),
    MODERATOR(5, "BLUE", "MODERATOR"),
    ADMIN(8, "RED", "ADMIN"),
    DEVELOPER(9, "YELLOW", "DEVELOPER"),
    OWNER(10, "DARK_RED", "OWNER");

    @Getter
    private int index;
    @Getter
    private String colour;
    @Getter
    private String friendlyName;

    PlayerRank(int index, String colour, String friendlyName) {
        this.index = index;
        this.colour = colour;
        this.friendlyName = friendlyName;
    }

    public boolean has(PlayerRank rank) {
        return index >= rank.getIndex();
    }
}
