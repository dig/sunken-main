package net.sunken.common.trigger;

import java.util.UUID;

public interface TriggerListener {

    void successfulTrigger(UUID playerUUID);

    Trigger.Condition getStartCondition();
}