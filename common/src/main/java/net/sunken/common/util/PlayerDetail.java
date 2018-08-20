package net.sunken.common.util;

import java.io.Serializable;
import java.util.UUID;

public class PlayerDetail implements Serializable {

    public final UUID uuid;
    public final String name;

    public PlayerDetail(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }
}
