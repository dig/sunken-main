package me.minigames.common.database;

public abstract class Database<C> {
    public abstract C getConnection();

    public abstract void disconnect();

//    public void runAsync(Runnable runnable) {
//        Bukkit.getScheduler().runTaskAsynchronously(MinigamePlugin.getInstance(), runnable);
//    }
//
//    public <U> CompletableFuture<U> runAsync(Supplier<U> supplier) {
//        return CompletableFuture.supplyAsync(supplier);
//    }
//
//    public <U, T> CompletableFuture<T> runAsync(Supplier<U> supplier, Function<U, T> function) {
//        return runAsync(supplier).thenApply(function);
//    }
}
