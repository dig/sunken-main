package me.minigames.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public enum CommandManager {
    INSTANCE;

    /*
     * All classes which have commands in them
     */
    private Set<Class> commandClasses = new HashSet<>();

    /*
     * Register a class containing commands
     */
    public void register(Class clazz) {
        commandClasses.add(clazz);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int argsLength = args.length;

        for (Class clazz : commandClasses) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(CommandDefinition.class)) {
                    CommandDefinition definition = method.getAnnotation(CommandDefinition.class);
                    if (command.getName().equalsIgnoreCase(definition.name())) {
                        if (argsLength >= definition.minArgs() && argsLength <= definition.maxArgs()) {
                            if (method.isAnnotationPresent(CommandAccess.class)) {
                                String requiredPermission = method.getAnnotation(CommandAccess.class).value();
                                if (!sender.hasPermission(requiredPermission)) {
                                    sender.sendMessage(ChatColor.RED + "You do not have the required permission!");
                                    return false;
                                }
                            }

                            CommandSpecimen specimen = new CommandSpecimen(sender, args);
                            if (method.isAnnotationPresent(CommandPlayer.class)) {
                                specimen = new PlayerCommandSpecimen(sender, args);
                            }

                            try {
                                method.invoke(null, specimen);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                Bukkit.getLogger().log(Level.SEVERE, "Unable to execute command. Possible incorrect command definition.");
                                e.printStackTrace();
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Wrong number of arguments.");
                            sender.sendMessage(ChatColor.RED + definition.usage());
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }
}
