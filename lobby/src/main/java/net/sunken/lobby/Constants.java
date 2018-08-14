package net.sunken.lobby;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String TAB_TOP = ChatColor.BLUE + "&lSUNKEN \n" + ChatColor.GRAY + "Home of unique minigames";
    public static final String TAB_BOTTOM = ChatColor.AQUA + "      &lSTORE.SUNKEN.NET      ";

    public static final List<String> JOIN_MESSAGES = Arrays.asList(
            " ",
            ChatColor.GRAY + "Welcome to " + ChatColor.BLUE + "&lSUNKEN&7.net",
            ChatColor.AQUA + "Home of unique minigames",
            " ",
            ChatColor.GRAY + "Visit the forums for the latest news",
            ChatColor.BLUE + "https://sunken.net",
            " "
    );

}
