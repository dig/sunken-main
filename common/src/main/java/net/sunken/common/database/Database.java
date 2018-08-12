package net.sunken.common.database;

import net.sunken.common.Common;
import org.bukkit.Bukkit;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Database<C> {
    public abstract C getConnection();

    public abstract void disconnect();

    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Common.getInstance(), runnable);
    }

    public <U> CompletableFuture<U> runAsync(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier);
    }

    public <U, T> CompletableFuture<T> runAsync(Supplier<U> supplier, Function<U, T> function) {
        return runAsync(supplier).thenApply(function);
    }
}
