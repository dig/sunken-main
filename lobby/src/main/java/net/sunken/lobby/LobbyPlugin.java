package net.sunken.lobby;

import com.sk89q.minecraft.util.commands.*;
import com.sk89q.minecraft.util.commands.cooldowns.OnCooldownException;
import com.sk89q.minecraft.util.commands.playerrank.PlayerNotHasRankException;
import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.event.EventManager;
import net.sunken.common.event.example.ExampleListener;
import net.sunken.common.packet.PacketHandlerRegistry;
import net.sunken.common.parkour.ParkourLeaderboardUpdatePacket;
import net.sunken.common.type.ServerType;
import net.sunken.core.Core;
import net.sunken.core.inventory.Page;
import net.sunken.core.inventory.PageContainer;
import net.sunken.lobby.listeners.LobbyPlayerCountUpdater;
import net.sunken.lobby.listeners.PlayerListener;
import net.sunken.lobby.listeners.WorldListener;
import net.sunken.lobby.parkour.ParkourCache;
import net.sunken.lobby.parkour.ParkourHandler;
import net.sunken.lobby.parkour.ParkourListener;
import net.sunken.lobby.player.ScoreboardUpdateListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyPlugin extends JavaPlugin implements CommandExecutor {

    @Getter
    private static LobbyPlugin instance;

    private ParkourCache parkourCache;

    @Getter
    private PageContainer lobbyInventory;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        Common.getInstance().onCommonLoad(true, ServerType.valueOf(this.getConfig().getString("type")),
                                          Bukkit.getMaxPlayers(), Bukkit.getPort());

        Core.getInstance().onCoreLoad(this);

        PacketHandlerRegistry.registerHandler(ParkourLeaderboardUpdatePacket.class, new ParkourHandler());

        this.parkourCache = new ParkourCache(this.getConfig());

        this.lobbyInventory = new PageContainer();
        Page lobbies = Page.builder("lobby-selector")
                .title("Lobby Selector")
                .size(27)
                .build();

        this.registerEvents();
    }

    @Override
    public void onDisable() {
        this.parkourCache.cleanupParkours();
        Core.getInstance().onCoreDisable();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        try {
            Core.getInstance().getCommands().execute(cmd.getName(), args, sender, sender);
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
            sender.sendMessage("You have no permission!");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(e.getMessage());
            sender.sendMessage(e.getUsage());
        } catch (CommandNumberFormatException e) {
            sender.sendMessage("Number expected, got " + e.getActualText() + " instead.");
        } catch (PlayerNotHasRankException e) {
            sender.sendMessage("You must be " + e.getRequired().getFriendlyName() + " to execute this command!");
        } catch (OnCooldownException e) {
            sender.sendMessage("You are on a cooldown!");
        } catch (WrappedCommandException e) {
            sender.sendMessage(e.getMessage());
        } catch (CommandException e) {
            if (e.getMessage() != null) {
                sender.sendMessage(e.getMessage());
            } else {
                sender.sendMessage("Something went wrong!");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            sender.sendMessage("Something went wrong!");
        }
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new LobbyPlayerCountUpdater(), this);
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new WorldListener(), this);
        pm.registerEvents(new ParkourListener(), this);

        EventManager.register(new ScoreboardUpdateListener());
    }

    public ParkourCache getParkourCache() {
        return this.parkourCache;
    }
}
