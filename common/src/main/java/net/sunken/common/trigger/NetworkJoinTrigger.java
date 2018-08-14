package net.sunken.common.trigger;

import net.sunken.common.player.AbstractPlayer;

import javax.annotation.Nullable;

public class NetworkJoinTrigger extends Trigger {

    public void trigger(AbstractPlayer player, @Nullable Boolean joinedBefore) {
        for (TriggerListener listener : TriggerListenerRegistry.all()) {
            if (listener.getSuccessCondition() instanceof Condition) {
                Condition condition = (Condition) listener.getSuccessCondition();
                if (condition.isProposalMet(new Proposal(joinedBefore))) {
                    listener.onSuccessfulTrigger(player);
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
