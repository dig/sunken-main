package net.sunken.common.achievements;

import net.sunken.common.trigger.NetworkJoinTrigger;

public class NetworkFirstJoinAchievement extends Achievement {

    public NetworkFirstJoinAchievement() {
        super("NETWORK_FIRST_JOIN",
              "Network First Join",
              "Join the network for the first time",
              new NetworkJoinTrigger.Condition(false));
    }
}
