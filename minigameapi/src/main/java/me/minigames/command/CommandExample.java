package me.minigames.command;

import org.bukkit.entity.Player;

public class CommandExample {
    @CommandDefinition(name = "hub", usage = "/hub", minArgs = 0, maxArgs = 0)
    public static void hubCommand(CommandSpecimen specimen) {
        // ...
    }

    @CommandDefinition(name = "hub", usage = "/hub", minArgs = 0, maxArgs = 0)
    @CommandPlayer
    public static void hubCommand(PlayerCommandSpecimen specimen) {
        Player p = specimen.getPlayer();
        // ...
    }

    @CommandDefinition(name = "tp", usage = "/tp", minArgs = 0, maxArgs = 0)
    @CommandPlayer
    @CommandAccess("minigames.tp")
    public static void tpCommand(PlayerCommandSpecimen specimen) {
        Player p = specimen.getPlayer();
        // ...
    }

    @CommandDefinition(name = "broadcast", usage = "/broadcast", minArgs = 1)
    @CommandPlayer
    @CommandAccess("minigames.broadcast")
    public static void broadcastCommand(PlayerCommandSpecimen specimen) {
        Player p = specimen.getPlayer();
        String broadcastMessage = specimen.concatString(0, " ");
        // ...
    }
}
