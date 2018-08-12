package me.minigames;

import lombok.Getter;
import me.minigames.command.CommandManager;
import me.minigames.common.database.MongoConnection;
import me.minigames.common.database.RedisConnection;
import me.minigames.lobby.LobbyListener;
import me.minigames.lobby.LobbyManager;
import me.minigames.lobby.task.DayTask;
import me.minigames.lobby.task.StartPreconditionsTask;
import me.minigames.npc.NPCListener;
import me.minigames.utils.BungeeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MinigamePlugin extends JavaPlugin {

    @Getter
    private static MinigamePlugin instance;

    @Getter
    private MongoConnection mongo;
    @Getter
    private RedisConnection redis;
    @Getter
    private Minigame minigame;

    @Override
    public void onEnable() {
        instance = this;

        this.mongo = new MongoConnection("localhost", 27017, "username", "password");
        this.redis = new RedisConnection("localhost", 3306, "password");

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        this.registerEvents();

        new DayTask().runTaskTimer(this, 0L, DayTask.RUN_EVERY);
        new StartPreconditionsTask().runTaskTimer(this, 0L, StartPreconditionsTask.RUN_EVERY);

        LobbyManager.loadLobbySchematic();

        this.onCustomEnable();
    }

    @Override
    public void onDisable() {
        this.onCustomDisable();

        Bukkit.getOnlinePlayers().forEach(BungeeUtil::sendPlayerToLobby);

        this.mongo.disconnect();
        this.redis.disconnect();

        Bukkit.getScheduler().cancelAllTasks();
    }

    private void registerEvents() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new LobbyListener(), this);
        pm.registerEvents(new NPCListener(), this);
    }

    public abstract void onCustomEnable();

    public abstract void onCustomDisable();

    /*
     * Start the minigame
     */
    public abstract void startGame();

    /*
     * Enable the custom command handler
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return CommandManager.INSTANCE.onCommand(sender, command, label, args);
    }
}
