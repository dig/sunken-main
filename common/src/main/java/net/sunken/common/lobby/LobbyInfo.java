package net.sunken.common.lobby;

import com.google.common.base.Objects;
import lombok.Getter;

public class LobbyInfo {

    @Getter
    private final String serverName;
    @Getter
    private final int playerCount;

    public LobbyInfo(String serverName, int playerCount) {
        this.serverName = serverName;
        this.playerCount = playerCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        LobbyInfo other = (LobbyInfo) obj;
        return Objects.equal(this.getServerName(), other.getServerName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getServerName(), this.getServerName());
    }
}
