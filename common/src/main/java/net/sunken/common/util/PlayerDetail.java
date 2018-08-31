package net.sunken.common.util;

import net.sunken.common.player.PlayerRank;

import java.io.Serializable;
import java.util.UUID;

public class PlayerDetail implements Serializable {

    public UUID uuid;
    public String name;
    public PlayerRank rank;

    public PlayerDetail(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.rank = PlayerRank.USER;
    }

    public PlayerDetail(UUID uuid, String name, PlayerRank rank) {
        this(uuid, name);
        this.rank = rank;
    }
}
