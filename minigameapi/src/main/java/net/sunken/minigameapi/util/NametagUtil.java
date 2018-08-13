package net.sunken.minigameapi.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Created by Digital on 22/01/2018.
 */

public class NametagUtil {

    public enum TeamAction {
        CREATE, DESTROY, UPDATE
    }

    private static Team team;
    private static Scoreboard scoreboard;

    public static void changePlayerName(Player player, String prefix, String suffix, TeamAction action) {
        if (player.getScoreboard() == null || prefix == null || suffix == null || action == null) {
            return;
        }

        scoreboard = player.getScoreboard();

        if (scoreboard.getTeam(player.getName()) == null) {
            scoreboard.registerNewTeam(player.getName());
        }

        team = scoreboard.getTeam(player.getName());
        team.setPrefix(Color(prefix));
        team.setSuffix(Color(suffix));
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);

        switch (action) {
            case CREATE:
                team.addEntry(player.getName());
                break;
            case UPDATE:
                team.unregister();
                scoreboard.registerNewTeam(player.getName());
                team = scoreboard.getTeam(player.getName());
                team.setPrefix(Color(prefix));
                team.setSuffix(Color(suffix));
                team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
                team.addEntry(player.getName());
                break;
            case DESTROY:
                team.unregister();
                break;
        }
    }

    private static String Color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}
