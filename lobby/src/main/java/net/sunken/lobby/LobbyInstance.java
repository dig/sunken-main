package net.sunken.lobby;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.lobby.LobbyInfo;
import org.bukkit.Bukkit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.logging.Level;

public class LobbyInstance {

    private static LobbyInstance instance;
    @Getter
    private LobbyInfo lobbyInfo;

    public void inform(int count) {
        LobbyInfo lobbyInfo = LobbyInstance.instance().getLobbyInfo();
        LobbyInfo updatedLobbyInfo = lobbyInfo.setPlayerCount(count);

        Common.getInstance().getLobbyChangeInformer().inform(updatedLobbyInfo);
    }

    public static LobbyInstance instance() {
        if (instance == null) {
            // lazy initialization so one is only created
            // when the plugin is enabled
            instance = new LobbyInstance();
        }
        return instance;
    }

    private LobbyInstance() {
        try {
            String uuid = UUID.randomUUID().toString();

            lobbyInfo = new LobbyInfo(uuid,
                                      Bukkit.getOnlinePlayers().size(),
                                      InetAddress.getLocalHost().getHostAddress(),
                                      Bukkit.getPort());

            Common.getLogger().log(Level.INFO, "Server starting with UUID: " + uuid);
            Common.getInstance().getLobbyChangeInformer().inform(lobbyInfo); // initial inform on creation
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
