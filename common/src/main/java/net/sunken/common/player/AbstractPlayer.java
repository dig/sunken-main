package net.sunken.common.player;

import lombok.Getter;

public abstract class AbstractPlayer {

    @Getter
    private String uuid;
    @Getter
    private String name;

    public void AbstractPlayer(String uuid, String name){
        this.uuid = uuid;
        this.name = name;
    }

}
