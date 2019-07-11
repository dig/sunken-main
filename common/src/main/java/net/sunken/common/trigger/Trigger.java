package net.sunken.common.trigger;

public abstract class Trigger {

    public abstract static class Condition<T extends Proposal> {

        public abstract boolean isProposalMet(T proposal);
    }

    public abstract static class Proposal {
    }
}
