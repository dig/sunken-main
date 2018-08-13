package net.sunken.common.lobby;

import com.google.common.base.Objects;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Immutable
public class LobbyInfo {

    @Getter
    private final String serverName;

    @Getter
    private int maxPlayers;
    @Getter
    private int playerCount;

    @Getter
    private final String serverIp;
    @Getter
    private final int serverPort;

    public LobbyInfo(String serverName, int maxPlayers, int playerCount, String serverIp, int serverPort) {
        this.serverName = serverName;

        this.maxPlayers = maxPlayers;
        this.playerCount = playerCount;

        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    // player count is the only mutable property of this immutable class
    public LobbyInfo setPlayerCount(int playerCount) {
        return new LobbyInfo(serverName,
                             maxPlayers,
                             playerCount,
                             serverIp,
                             serverPort);
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
