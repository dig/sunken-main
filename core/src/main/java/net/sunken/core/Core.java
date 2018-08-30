package net.sunken.core;

import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import lombok.Getter;
import net.sunken.common.event.EventManager;
import net.sunken.core.hologram.HologramInteractListener;
import net.sunken.core.inventory.element.ElementListener;
import net.sunken.core.model.ModelCommand;
import net.sunken.core.model.WalkTask;
import net.sunken.core.model.listener.ModelListener;
import net.sunken.core.npc.NPCListener;
import net.sunken.core.server.ServersCommand;
import net.sunken.core.server.ServersListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.logging.Level;

public class Core {

    @Getter
    private CommandsManager<CommandSender> commands;

    @Getter
    private static Core instance = new Core();

    @Getter
    private static Plugin plugin;

    public void onCoreLoad(Plugin plugin) {
        Core.plugin = plugin;

        this.registerEvents();
        this.setupCommands(plugin);

        // Used to enable players to walk on walkable models.
        WalkTask walkTask = new WalkTask();
        walkTask.runTaskTimer(plugin, 0l, 2l);
    }

    public void onCoreDisable() {
    }

    private void setupCommands(Plugin plugin) {
        this.commands = new BukkitCommandsManager();
        CommandsManagerRegistration registry = new CommandsManagerRegistration(plugin, this.commands);

        // register all commands
        registry.register(ModelCommand.Parent.class);
        registry.register(ServersCommand.class);
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new NPCListener(), plugin);
        pm.registerEvents(new HologramInteractListener(), plugin);
        pm.registerEvents(new ElementListener(), plugin);
        pm.registerEvents(new ModelListener(), plugin);

        EventManager.register(new ServersListener());

        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
    }

    private Core() {
    }
}
