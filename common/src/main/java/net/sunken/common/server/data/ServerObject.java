package net.sunken.common.server.data;

import com.google.common.base.Objects;
import lombok.Getter;
import net.sunken.common.type.ServerType;

import javax.annotation.concurrent.Immutable;

@Immutable
public class ServerObject {

    @Getter
    private final String serverName;
    @Getter
    private final ServerType serverType;

    @Getter
    private int maxPlayers;
    @Getter
    private int playerCount;

    @Getter
    private final String serverIp;
    @Getter
    private final int serverPort;

    @Getter
    private final Long created;

    public ServerObject(String serverName,
                        ServerType serverType,
                        int maxPlayers,
                        int playerCount,
                        String serverIp,
                        int serverPort,
                        long created) {

        this.serverName = serverName;
        this.serverType = serverType;

        this.maxPlayers = maxPlayers;
        this.playerCount = playerCount;

        this.serverIp = serverIp;
        this.serverPort = serverPort;

        this.created = created;
    }

    // player count is the only mutable property of this immutable class
    public ServerObject setPlayerCount(int playerCount) {
        return new ServerObject(serverName,
                                serverType,
                                maxPlayers,
                                playerCount,
                                serverIp,
                                serverPort,
                                created);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        ServerObject other = (ServerObject) obj;
        return Objects.equal(this.getServerName(), other.getServerName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getServerName());
    }
}
