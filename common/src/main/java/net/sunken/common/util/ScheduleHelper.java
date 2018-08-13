package net.sunken.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class ScheduleHelper {

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    public static ScheduledExecutorService executor() {
        return executor;
    }

    private ScheduleHelper() {
        throw new AssertionError("You must not attempt to instantiate this class.");
    }
}
