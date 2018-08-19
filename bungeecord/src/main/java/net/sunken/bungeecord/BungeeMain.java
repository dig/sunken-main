package net.sunken.bungeecord;

import com.sk89q.bungee.util.BungeeCommandsManager;
import com.sk89q.bungee.util.CommandExecutor;
import com.sk89q.bungee.util.CommandRegistration;
import com.sk89q.minecraft.util.commands.*;
import com.sk89q.minecraft.util.commands.playerrank.PlayerNotHasRankException;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.sunken.bungeecord.config.ConfigHandler;
import net.sunken.bungeecord.listeners.ConnectListener;
import net.sunken.bungeecord.listeners.FailListener;
import net.sunken.bungeecord.listeners.JoinListener;
import net.sunken.bungeecord.listeners.PingListener;
import net.sunken.bungeecord.party.PartyCommand;
import net.sunken.bungeecord.party.PartyInviteSendPacketHandler;
import net.sunken.bungeecord.server.LobbyCommand;
import net.sunken.common.Common;
import net.sunken.common.packet.PacketHandlerRegistry;
import net.sunken.common.parties.packet.PartyInviteSendPacket;

public class BungeeMain extends Plugin implements CommandExecutor<CommandSender> {


    private CommandsManager<CommandSender> commands;

    @Getter
    private static BungeeMain instance;

    @Getter
    private static ConfigHandler configHandler;

    @Override
    public void onEnable() {
        instance = this;

        PacketHandlerRegistry.registerHandler(new PartyInviteSendPacket(), new PartyInviteSendPacketHandler());

        // Config Handler
        configHandler = new ConfigHandler(this, "config.yml");

        // Initialize common
        Common.getInstance().onCommonLoad(true);
        // Register events
        this.registerEvents();

        // Get information about running servers
        Common.getInstance().getServerCache().updateCache();

        this.setupCommands();
    }

    @Override
    public void onDisable() {
        Common.getInstance().onCommonDisable();
    }

    private void setupCommands() {
        this.commands = new BungeeCommandsManager();
        CommandRegistration registry = new CommandRegistration(this, this.getProxy().getPluginManager(), this.commands,
                                                               this);
        // register all commands
        registry.register(PartyCommand.class);
        registry.register(LobbyCommand.class);
    }

    @Override
    public void onCommand(CommandSender sender, String commandName, String[] args) {
        try {
            this.commands.execute(commandName, args, sender, sender);
        } catch (Throwable e) {
            this.handleException(sender, e);
        }
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
        PluginManager pm = this.getProxy().getPluginManager();

        pm.registerListener(this, new JoinListener());
        pm.registerListener(this, new ConnectListener());
        pm.registerListener(this, new PingListener());
        pm.registerListener(this, new FailListener());
    }
}
