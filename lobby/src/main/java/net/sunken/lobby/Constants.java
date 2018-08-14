package net.sunken.lobby;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String TAB_TOP = ChatColor.BLUE + "&lSUNKEN \n" + ChatColor.GRAY + "Home of unique minigames";
    public static final String TAB_BOTTOM = ChatColor.AQUA + "      &lSTORE.SUNKEN.NET      ";

    public static final List<String> JOIN_MESSAGES = Arrays.asList(
            ChatColor.GRAY + "&m                               ",
            " ",
            ChatColor.BLUE + "&lSUNKEN",
            " ",
            ChatColor.GRAY + "Sunken is a minigames network with unique",
            ChatColor.GRAY + "minigames to play! To get started, right click",
            ChatColor.GRAY + "an NPC or use your compass to join a game.",
            " ",
            ChatColor.YELLOW + "Visit our forums: https://sunken.net",
            " ",
            ChatColor.GRAY + "&m                               "
    );

}
