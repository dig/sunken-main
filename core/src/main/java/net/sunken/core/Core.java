package net.sunken.core;

import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandRegistration;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import com.sk89q.minecraft.util.commands.cooldowns.OnCooldownException;
import com.sk89q.minecraft.util.commands.playerrank.PlayerNotHasRankException;
import lombok.Getter;
import net.sunken.core.hologram.HologramInteractListener;
import net.sunken.core.inventory.element.ElementListener;
import net.sunken.core.model.ModelCommand;
import net.sunken.core.npc.NPCListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

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
    }

    public void onCoreDisable() {
    }

    private void setupCommands(Plugin plugin) {
        this.commands = new BukkitCommandsManager();
        CommandsManagerRegistration registry = new CommandsManagerRegistration(plugin, this.commands);

        // register all commands
        registry.register(ModelCommand.class);
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new NPCListener(), plugin);
        pm.registerEvents(new HologramInteractListener(), plugin);
        pm.registerEvents(new ElementListener(), plugin);
    }

    private Core() {
    }
}
