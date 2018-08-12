package net.sunken.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class AsyncHelper {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static ExecutorService executor() {
        return executor;
    }

    private AsyncHelper() {
        throw new AssertionError("You must not attempt to instantiate this class.");
    }
}
