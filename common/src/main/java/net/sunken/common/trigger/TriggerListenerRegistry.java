package net.sunken.common.trigger;

import com.google.common.collect.Sets;

import java.util.Set;

public final class TriggerListenerRegistry {

    private static final Set<TriggerListener> listeners;

    static {
        listeners = Sets.newHashSet();
    }

    public static void addListener(TriggerListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(TriggerListener listener) {
        listeners.remove(listener);
    }

    public static Set<TriggerListener> all() {
        return listeners;
    }

    private TriggerListenerRegistry() {
    }
}