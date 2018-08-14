package net.sunken.common.achievements;

import net.sunken.common.trigger.Trigger;
import net.sunken.common.trigger.TriggerListener;
import net.sunken.common.trigger.TriggerListenerRegistry;

import java.util.UUID;

public class NetworkJoinTrigger extends Trigger {

    public void trigger(UUID uuid) {
        for (TriggerListener listener : TriggerListenerRegistry.all()) {
            if (listener.getStartCondition() instanceof Condition) {
                Condition condition = (Condition) listener.getStartCondition();
                if (condition.isProposalMet(new Proposal())) {
                    listener.successfulTrigger(uuid);
                }
            }
        }
    }

    public static class Condition extends Trigger.Condition<Proposal> {

        @Override
        public boolean isProposalMet(Proposal proposal) {
            return true;
        }
    }

    public static class Proposal extends Trigger.Proposal {
    }
}
