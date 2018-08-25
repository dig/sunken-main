package net.sunken.bungeecord;

import com.sk89q.bungee.util.BungeeCommandsManager;
import com.sk89q.bungee.util.CommandExecutor;
import com.sk89q.bungee.util.CommandRegistration;
import com.sk89q.minecraft.util.commands.*;
import com.sk89q.minecraft.util.commands.cooldowns.OnCooldownException;
import com.sk89q.minecraft.util.commands.playerrank.PlayerNotHasRankException;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.sunken.bungeecord.config.ConfigHandler;
import net.sunken.bungeecord.friend.FriendAcceptStatusHandler;
import net.sunken.bungeecord.friend.FriendCommand;
import net.sunken.bungeecord.friend.FriendStatusHandler;
import net.sunken.bungeecord.listeners.ConnectListener;
import net.sunken.bungeecord.listeners.FailListener;
import net.sunken.bungeecord.listeners.JoinListener;
import net.sunken.bungeecord.listeners.PingListener;
import net.sunken.bungeecord.party.*;
import net.sunken.bungeecord.server.LobbyCommand;
import net.sunken.common.Common;
import net.sunken.common.friend.packet.FriendAcceptStatusPacket;
import net.sunken.common.friend.packet.FriendStatusPacket;
import net.sunken.common.packet.PacketHandlerRegistry;
import net.sunken.common.packet.SendPlayerBungeeMessagePacket;
import net.sunken.common.parties.packet.MustBeInPartyPacket;
import net.sunken.common.parties.packet.MustBeLeaderPacket;
import net.sunken.common.parties.packet.changes.*;

public class BungeeMain extends Plugin implements CommandExecutor<CommandSender> {

    private CommandsManager<CommandSender> commands;

    @Getter
    private static BungeeMain instance;

    @Getter
    private static ConfigHandler configHandler;

    @Override
    public void onEnable() {
        instance = this;

        // Config Handler
        configHandler = new ConfigHandler(this, "config.yml");

        // Initialize common
        Common.getInstance().onCommonLoad(true);

        // Register events
        this.registerEvents();

        // Register packet handlers
        PacketHandlerRegistry.registerHandler(PartyInviteValidatedPacket.class, new PartyInviteValidateActor());
        PacketHandlerRegistry.registerHandler(FriendStatusPacket.class, new FriendStatusHandler());
        PacketHandlerRegistry.registerHandler(FriendAcceptStatusPacket.class, new FriendAcceptStatusHandler());
        PacketHandlerRegistry.registerHandler(PartyInviteExpiredPacket.class, new PartyInviteExpiredActor());
        PacketHandlerRegistry.registerHandler(PartyCreatedPacket.class, new PartyCreatedActor());
        PacketHandlerRegistry.registerHandler(PartyMemberJoinedPacket.class, new PartyMemberAddActor());
        PacketHandlerRegistry.registerHandler(MustBeInPartyPacket.class, new MustBeInPartyActor());
        PacketHandlerRegistry.registerHandler(MustBeLeaderPacket.class, new MustBeLeaderActor());
        PacketHandlerRegistry.registerHandler(PartyListPacket.class, new PartyListActor());
        PacketHandlerRegistry.registerHandler(SendPlayerBungeeMessagePacket.class, new SendPlayerBungeeMessageHandler());
        PacketHandlerRegistry.registerHandler(PartyMemberLeftPacket.class, new PartyLeaveActor());
        PacketHandlerRegistry.registerHandler(PartyDisbandedPacket.class, new PartyDisbandActor());

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
        CommandRegistration registry = new CommandRegistration(this,
                this.getProxy().getPluginManager(),
                this.commands,
                this);
        // register all commands
        registry.register(PartyCommands.Parent.class);
        registry.register(LobbyCommand.class);
        registry.register(FriendCommand.class);
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
        } catch (OnCooldownException e) {
            sender.sendMessage(new TextComponent("You are on a cooldown!"));
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
