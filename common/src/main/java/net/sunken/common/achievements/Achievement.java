package net.sunken.common.achievements;

import lombok.Getter;
import net.sunken.common.trigger.Trigger;
import net.sunken.common.trigger.TriggerListener;

import java.util.UUID;

public class Achievement implements AchievementInformation, TriggerListener {

    /** ID of the achievement */
    private final String id;
    /** Name of the achievement */
    private final String name;
    /** Short description of the achievement */
    private final String description;
    /** The condition required that has to be met for this achievement to be awarded */
    @Getter
    private final Trigger.Condition condition;

    public Achievement(String id, String name, String description, Trigger.Condition condition) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.condition = condition;
    }

    @Override
    public void successfulTrigger(UUID playerUUID) {
        // award this achievement to the player
    }

    @Override
    public Trigger.Condition getStartCondition() {
        return this.condition;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}