package net.sunken.common.trigger;

import net.sunken.common.player.AbstractPlayer;

public interface TriggerListener {

    void onSuccessfulTrigger(AbstractPlayer player, int progressToAdd);

    Trigger.Condition getSuccessCondition();
}