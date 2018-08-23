package net.sunken.common.player;

import lombok.Getter;

public enum PlayerRank {

    USER(0, "GRAY", "Member"),
    MODERATOR(5, "BLUE", "Moderator"),
    ADMIN(8, "RED", "Admin"),
    DEVELOPER(9, "YELLOW", "Developer"),
    OWNER(10, "DARK_RED", "Owner");

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
