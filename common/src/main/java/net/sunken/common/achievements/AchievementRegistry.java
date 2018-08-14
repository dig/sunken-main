package net.sunken.common.achievements;

import com.google.common.collect.Maps;
import net.sunken.common.trigger.TriggerListenerRegistry;

import java.util.Map;

public final class AchievementRegistry {

    private static final Map<String, Achievement> achievements;

    static {
        achievements = Maps.newHashMap();
    }

    public static void addAchievement(Achievement achievement) {
        achievements.put(achievement.getId(), achievement);
        TriggerListenerRegistry.addListener(achievement);
    }

    public static Map<String, Achievement> allAchievements() {
        return achievements;
    }

    private AchievementRegistry() {
    }
}
