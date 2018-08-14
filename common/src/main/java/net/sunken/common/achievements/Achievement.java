package net.sunken.common.achievements;

import com.google.common.base.Objects;
import lombok.Getter;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.trigger.Trigger;
import net.sunken.common.trigger.TriggerListener;

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
    public void onSuccessfulTrigger(AbstractPlayer player) {
        player.grantAchievement(this);
    }

    @Override
    public Trigger.Condition getSuccessCondition() {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        Achievement other = (Achievement) obj;
        return Objects.equal(this.getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }
}