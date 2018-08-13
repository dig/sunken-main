package net.sunken.lobby;

import lombok.Getter;
import net.sunken.common.lobby.LobbyInfo;
import org.bukkit.Bukkit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class LobbyInstance {

    private static LobbyInstance instance;
    @Getter
    private LobbyInfo lobbyInfo;

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
            lobbyInfo = new LobbyInfo(UUID.randomUUID().toString(),
                                      Bukkit.getOnlinePlayers().size(),
                                      InetAddress.getLocalHost().getHostAddress(),
                                      Bukkit.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}