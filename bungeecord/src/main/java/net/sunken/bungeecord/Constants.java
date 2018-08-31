package net.sunken.bungeecord;

import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String NO_LOBBY = "&cThere are no available lobbies!";

    public static final String MOTD_TOP_LINE = ChatColor.BLUE + "&lSunken &7| " + ChatColor.AQUA + "&l1.13 SUPPORT";
    public static final String MOTD_BOTTOM_LINE = "&a&lHOME OF &6&lUNIQUE &a&lMINIGAMES";

    public static final String OUTDATED_VER = "&cWe currently do not support the version of minecraft you are on.";

    public static final int MAX_PLAYERS = 10000;

    public static final List<String> JOIN_MESSAGES = Arrays.asList(
            ChatColor.GRAY + "&m                               ",
            " ",
            ChatColor.BLUE + "&lSUNKEN",
            " ",
            ChatColor.GRAY + "Sunken is a network with unique minigames",
            ChatColor.GRAY + "to play! To get started, right click an",
            ChatColor.GRAY + "NPC or use your compass to join a game.",
            " ",
            ChatColor.YELLOW + "Visit our forums: https://sunken.net",
            " ",
            ChatColor.GRAY + "&m                               "
    );
}
