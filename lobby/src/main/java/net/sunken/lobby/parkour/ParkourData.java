package net.sunken.lobby.parkour;

import lombok.Getter;
import net.sunken.common.player.PlayerRank;

public class ParkourData {

    @Getter
    private String uuid;
    @Getter
    private String name;
    @Getter
    private PlayerRank rank;

    @Getter
    private String type;
    @Getter
    private Long time;

    public ParkourData (String uuid, String name, PlayerRank rank,
                       String type, Long time) {
        this.uuid = uuid;
        this.name = name;
        this.rank = rank;

        this.type = type;
        this.time = time;
    }

}
