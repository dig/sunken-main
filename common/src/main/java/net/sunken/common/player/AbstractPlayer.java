package net.sunken.common.player;

import lombok.Getter;
import net.sunken.common.achievements.Achievement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractPlayer {

    @Getter
    private String uuid;
    @Getter
    private String name;

    @Getter
    private final Map<String, Achievement> achievements;

    public AbstractPlayer(String uuid,
                          String name) {
        this.uuid = uuid;
        this.name = name;
        this.achievements = new HashMap<String, Achievement>();

        // Load achievements from mongo ...
    }

    public UUID getUUID(){
        return UUID.fromString(this.uuid);
    }
}
