package net.sunken.common.achievements;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class AchievementManager {

    @Getter
    private final Map<UUID, Map<String, Achievement>> loadedAchievements;

    {
        this.loadedAchievements = Maps.newHashMap();
    }

    public void loadPlayer(UUID playerUUID) {
        checkNotNull(playerUUID, "player UUID cannot be null");
        checkState(!loadedAchievements.containsKey(playerUUID), "player's achievements are already loaded");
    }

    public void unloadPlayer(UUID playerUUID) {
        checkNotNull(playerUUID, "player UUID cannot be null");
        checkState(loadedAchievements.containsKey(playerUUID), "player's achievements are not loaded");
    }
}
