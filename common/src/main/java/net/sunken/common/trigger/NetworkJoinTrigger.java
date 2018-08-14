package net.sunken.common.trigger;

import javax.annotation.Nullable;
import java.util.UUID;

public class NetworkJoinTrigger extends Trigger {

    public void trigger(UUID uuid, @Nullable Boolean joinedBefore) {
        for (TriggerListener listener : TriggerListenerRegistry.all()) {
            if (listener.getStartCondition() instanceof Condition) {
                Condition condition = (Condition) listener.getStartCondition();
                if (condition.isProposalMet(new Proposal(joinedBefore))) {
                    listener.successfulTrigger(uuid);
                }
            }
        }
    }

    public static class Condition extends Trigger.Condition<Proposal> {

        private final Boolean joinedBefore;

        public Condition(@Nullable Boolean joinedBefore) {
            this.joinedBefore = joinedBefore;
        }

        @Override
        public boolean isProposalMet(Proposal proposal) {
            if (this.joinedBefore != null) {
                return proposal.joinedBefore == this.joinedBefore;
            }

            return true;
        }
    }

    public static class Proposal extends Trigger.Proposal {

        private final Boolean joinedBefore;

        public Proposal(@Nullable Boolean joinedBefore) {
            this.joinedBefore = joinedBefore;
        }
    }
}
