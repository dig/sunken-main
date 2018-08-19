package net.sunken.common;

import lombok.Getter;
import net.sunken.common.player.AbstractPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {

    /* Name, UUID, names are stored in lowercase */
    @Getter
    private Map<String, UUID> nameToUUID;

    @Getter
    private Map<UUID, AbstractPlayer> onlinePlayers;

    public DataManager (boolean fetchPlayerData){
        this.nameToUUID = new ConcurrentHashMap<>();
        this.onlinePlayers = new ConcurrentHashMap<>();

        // Fetch from master here...
        if (fetchPlayerData) {

        }
    }

}
