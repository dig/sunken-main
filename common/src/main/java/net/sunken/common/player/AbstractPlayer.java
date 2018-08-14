package net.sunken.common.player;

import lombok.Getter;
import net.sunken.common.achievements.Achievement;

import java.util.Map;

public abstract class AbstractPlayer {

    @Getter
    private String uuid;
    @Getter
    private String name;

    @Getter
    private final Map<String, Achievement> achievements;

    public AbstractPlayer(String uuid,
                          String name,
                          Map<String, Achievement> achievements) {

        this.uuid = uuid;
        this.name = name;
        this.achievements = achievements;
    }
}
