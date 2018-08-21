package net.sunken.common;

import lombok.Getter;
import net.sunken.common.player.AbstractPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {

    /** Name, UUID; Names are stored in lowercase */
    @Getter
    private Map<String, UUID> nameToUUID;
    @Getter
    private Map<UUID, AbstractPlayer> onlinePlayers;

    public DataManager (){
        this.nameToUUID = new ConcurrentHashMap<>();
        this.onlinePlayers = new ConcurrentHashMap<>();
    }
}
