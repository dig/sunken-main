package net.sunken.common.trigger;

import net.sunken.common.player.AbstractPlayer;

public interface TriggerListener {

    void onSuccessfulTrigger(AbstractPlayer player);

    Trigger.Condition getSuccessCondition();
}