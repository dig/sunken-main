package net.sunken.common.type;

import lombok.Getter;

public enum ServerType {

    MAIN_LOBBY ("Main Lobby");

    @Getter
    private String friendlyName;

    ServerType (String friendlyName) {
        this.friendlyName = friendlyName;
    }
}
