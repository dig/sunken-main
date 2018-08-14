package net.sunken.common;

import lombok.Getter;
import lombok.Setter;
import net.sunken.common.server.ServerObject;
import net.sunken.common.type.ServerType;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.logging.Level;

public class ServerInstance {

    @Setter
    private static ServerInstance instance = null;

    @Getter
    private ServerObject serverObject;

    public void inform(int count) {
        ServerObject serverInfo = ServerInstance.instance().getServerObject();
        ServerObject updatedServerInfo = serverInfo.setPlayerCount(count);

        Common.getInstance().getServerChangeInformer().inform(updatedServerInfo);
    }

    public static ServerInstance instance() {
        return instance;
    }

    public ServerInstance(ServerType serverType, int maxPlayers, int playerCount, int serverPort) {
        try {
            String uuid = UUID.randomUUID().toString();

            serverObject = new ServerObject(uuid,
                    serverType,
                    maxPlayers,
                    playerCount,
                    InetAddress.getLocalHost().getHostAddress(),
                    serverPort);

            Common.getLogger().log(Level.INFO, "Server starting with UUID: " + uuid);
            Common.getInstance().getServerChangeInformer().inform(serverObject); // initial inform on creation
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
