package net.sunken.common.player.packet;

import lombok.Getter;

public class InitialFetchPacket {

    @Getter
    private String serverName;

    public InitialFetchPacket (String serverName) {
        this.serverName = serverName;
    }

}
