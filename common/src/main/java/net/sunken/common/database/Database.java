package net.sunken.common.database;

import net.sunken.common.util.AsyncHelper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Database<C> {

    public abstract C getConnection();

    public abstract void disconnect();

    public void runAsync(Runnable runnable) {
        AsyncHelper.executor().submit(runnable);
    }

    public <U> CompletableFuture<U> runAsync(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier, AsyncHelper.executor());
    }

    public <U, T> CompletableFuture<T> runAsync(Supplier<U> supplier, Function<U, T> function) {
        return this.runAsync(supplier).thenApply(function);
    }
}
