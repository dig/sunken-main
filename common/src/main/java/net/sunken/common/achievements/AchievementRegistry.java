package net.sunken.common.achievements;

import com.google.common.collect.Sets;

import java.util.Set;

public final class AchievementRegistry {

    private static final Set<Achievement> achievements;

    static {
        achievements = Sets.newHashSet();
    }

    public static void addAchievement(Achievement achievement) {
        achievements.add(achievement);
    }

    public static void removeListener(Achievement achievement) {
        achievements.remove(achievement);
    }

    public static Set<Achievement> allAchievements() {
        return achievements;
    }

    private AchievementRegistry() {
    }
}
