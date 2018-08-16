package net.sunken.lobby;

import net.sunken.core.inventory.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;

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

    public static final ItemBuilder ITEM_LOBBY = new ItemBuilder(Material.BOOK)
            .lores(Arrays.asList(" ",
                    ChatColor.GRAY + "Click me to open the lobby selector",
                    ChatColor.GRAY + "which allows you to select what",
                    ChatColor.GRAY + "lobby you want to go on!",
                    " ",
                    ChatColor.AQUA + "> Click to open!"))
            .addNBTString("menu", "lobby")
            .name(ChatColor.GREEN + "Lobby Selector");

    public static final ItemBuilder ITEM_SELECTOR = new ItemBuilder(Material.COMPASS)
            .lores(Arrays.asList(" ",
                    ChatColor.GRAY + "Click me to open the minigame selector",
                    ChatColor.GRAY + "which allows you to select what",
                    ChatColor.GRAY + "minigame you want to play!",
                    " ",
                    ChatColor.AQUA + "> Click to open!"))
            .addNBTString("menu", "selector")
            .name(ChatColor.GOLD + "Minigame Selector");

}
