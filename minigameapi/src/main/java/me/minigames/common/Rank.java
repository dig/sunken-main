package me.minigames.common;

import lombok.Getter;

public enum Rank {
    DEFAULT(0), MODERATOR(80), ADMIN(90), OWNER(100);

    @Getter
    private int id;

    Rank(int id){
        this.id = id;
    }

    // Rank.MODERATOR.has(Rank.DEFAULT) => 80 >= 0
    public boolean has(Rank rank) {
        return rank.getId() >= this.getId();
    }

    public static Rank getById(int id){
        for(Rank r : Rank.values()){
            if(r.getId() == id){
                return r;
            }
        }

        return null;
    }
}
