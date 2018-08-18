package net.sunken.lobby;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketHandlerRegistry;
import net.sunken.common.packet.packets.ParkourLeaderboardUpdatePacket;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.service.PartyService;
import net.sunken.common.parties.service.RedisPartyService;
import net.sunken.common.player.PlayerRank;
import net.sunken.common.type.ServerType;
import net.sunken.core.Core;
import net.sunken.core.inventory.PageContainer;
import net.sunken.lobby.listeners.LobbyPlayerCountUpdater;
import net.sunken.lobby.listeners.PlayerListener;
import net.sunken.lobby.listeners.WorldListener;
import net.sunken.lobby.parkour.ParkourCache;
import net.sunken.lobby.parkour.ParkourHandler;
import net.sunken.lobby.parkour.ParkourListener;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.logging.Level;

public class LobbyPlugin extends JavaPlugin {

    @Getter
    private static LobbyPlugin instance;

    private ParkourCache parkourCache;

    @Getter
    private Inventory lobbyInventory;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        Common.getInstance().onCommonLoad(true,
                ServerType.valueOf(this.getConfig().getString("type")),
                Bukkit.getMaxPlayers(), Bukkit.getPort());

        Core.getInstance().onCoreLoad(this);

        PacketHandlerRegistry.registerHandler(new ParkourLeaderboardUpdatePacket(), new ParkourHandler());

        this.parkourCache = new ParkourCache(this.getConfig());
        this.lobbyInventory = Bukkit.createInventory(null, 27, "Lobby Selector");

        this.registerEvents();

        // Test party
        UUID party = UUID.randomUUID();

        PartyService partyService = new RedisPartyService(Common.getInstance().getRedis().getJedisPool());
        PartyPlayer test = new PartyPlayer(UUID.randomUUID(), "test", PlayerRank.USER);
        PartyPlayer test1 = new PartyPlayer(UUID.randomUUID(), "test1", PlayerRank.USER);
        partyService.createParty(test, test1, party);


        Common.getLogger().log(Level.INFO, partyService.getPartyByUUID(party).toString());
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

    public ParkourCache getParkourCache(){
        return this.parkourCache;
    }
}
