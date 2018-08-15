package net.sunken.common.player;

import lombok.Getter;

public enum PlayerRank {

    USER(0, "GRAY"),
    MODERATOR(5, "BLUE"),
    ADMIN(8, "RED"),
    OWNER(10, "DARK_RED");

    @Getter
    private int index;

    @Getter
    private String colour;

    PlayerRank(int index, String colour){
        this.index = index;
        this.colour = colour;
    }

    public boolean has(PlayerRank rank){
        if(index >= rank.getIndex()){
            return true;
        }

        return false;
    }

}
