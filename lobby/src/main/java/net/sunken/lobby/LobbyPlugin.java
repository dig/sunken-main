package net.sunken.lobby;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.packet.PacketHandlerRegistry;
import net.sunken.common.packet.packets.ParkourLeaderboardUpdatePacket;
import net.sunken.common.type.ServerType;
import net.sunken.core.Core;
import net.sunken.core.inventory.Page;
import net.sunken.core.inventory.PageContainer;
import net.sunken.core.inventory.element.ActionableElement;
import net.sunken.core.inventory.element.Element;
import net.sunken.lobby.listeners.LobbyPlayerCountUpdater;
import net.sunken.lobby.listeners.PlayerListener;
import net.sunken.lobby.listeners.WorldListener;
import net.sunken.lobby.parkour.ParkourCache;
import net.sunken.lobby.parkour.ParkourHandler;
import net.sunken.lobby.parkour.ParkourListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyPlugin extends JavaPlugin {

    @Getter
    private static LobbyPlugin instance;

    private ParkourCache parkourCache;

    @Getter
    private PageContainer lobbyInventory;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        Common.getInstance().onCommonLoad(true, true,
                                          ServerType.valueOf(this.getConfig().getString("type")),
                                          Bukkit.getMaxPlayers(), Bukkit.getPort());

        Core.getInstance().onCoreLoad(this);

        PacketHandlerRegistry.registerHandler(new ParkourLeaderboardUpdatePacket(), new ParkourHandler());

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

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new LobbyPlayerCountUpdater(), this);
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new WorldListener(), this);
        pm.registerEvents(new ParkourListener(), this);
    }

    public ParkourCache getParkourCache() {
        return this.parkourCache;
    }
}
