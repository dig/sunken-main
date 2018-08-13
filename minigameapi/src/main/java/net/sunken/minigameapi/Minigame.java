package net.sunken.minigameapi;

import lombok.Getter;

public class Minigame {

    @Getter
    private static Minigame instance;

    public void onMinigameLoad(){
        instance = this;
    }

    public void onMinigameDisable(){

    }
}
