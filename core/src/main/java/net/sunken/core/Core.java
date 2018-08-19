package net.sunken.core;

import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandRegistration;
import com.sk89q.minecraft.util.commands.*;
import com.sk89q.minecraft.util.commands.playerrank.PlayerNotHasRankException;
import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;
import net.sunken.core.hologram.HologramInteractListener;
import net.sunken.core.inventory.element.ElementListener;
import net.sunken.core.npc.NPCListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Core implements CommandExecutor {

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
        CommandRegistration registry = new CommandRegistration(plugin, this);
        // register all commands
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
            return true;
        } catch (Throwable e) {
            this.handleException(sender, e);
        }
        return false;
    }

    private void handleException(CommandSender sender, Throwable throwable) {
        try {
            throw throwable;
        } catch (CommandPermissionsException e) {
            sender.sendMessage(new TextComponent("You have no permission!"));
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(new TextComponent(e.getUsage()));
        } catch (CommandUsageException e) {
            sender.sendMessage((new TextComponent(e.getMessage())));
            sender.sendMessage(new TextComponent(e.getUsage()));
        } catch (CommandNumberFormatException e) {
            sender.sendMessage(new TextComponent("Number expected, got " + e.getActualText() + " instead."));
        } catch (PlayerNotHasRankException e) {
            sender.sendMessage(new TextComponent(
                    "You must be " + e.getRequired().getFriendlyName() + " to execute this command!"));
        } catch (WrappedCommandException e) {
            sender.sendMessage(new TextComponent(e.getMessage()));
        } catch (CommandException e) {
            if (e.getMessage() != null) {
                sender.sendMessage(new TextComponent(e.getMessage()));
            } else {
                sender.sendMessage(new TextComponent("Something went wrong!"));
            }
        } catch (Throwable e) {
            sender.sendMessage(new TextComponent("Something went wrong!"));
        }
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
