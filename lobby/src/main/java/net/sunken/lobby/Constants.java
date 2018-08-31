package net.sunken.lobby;

import net.sunken.core.inventory.ItemBuilder;
import net.sunken.lobby.inventory.InventoryType;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String TAB_TOP = ChatColor.BLUE + "&lSUNKEN \n" + ChatColor.GRAY + "Home of unique minigames";
    public static final String TAB_BOTTOM = ChatColor.AQUA + "      &lSTORE.SUNKEN.NET      ";

    public static final String INVENTORY_NBT_KEY = "InventoryOpen";

    public static final ItemBuilder ITEM_LOBBY = new ItemBuilder(Material.BOOK)
            .lores(Arrays.asList(" ",
                    ChatColor.GRAY + "Click me to open the lobby selector",
                    ChatColor.GRAY + "which allows you to select what",
                    ChatColor.GRAY + "lobby you want to go on!",
                    " ",
                    ChatColor.AQUA + "> Click to open!"))
            .name(ChatColor.GREEN + "Lobby Selector")
            .addNBTString(INVENTORY_NBT_KEY, InventoryType.LOBBY.toString());

    public static final ItemBuilder ITEM_SELECTOR = new ItemBuilder(Material.COMPASS)
            .lores(Arrays.asList(" ",
                    ChatColor.GRAY + "Click me to open the minigame selector",
                    ChatColor.GRAY + "which allows you to select what",
                    ChatColor.GRAY + "minigame you want to play!",
                    " ",
                    ChatColor.AQUA + "> Click to open!"))
            .name(ChatColor.GOLD + "Minigame Selector")
            .addNBTString(INVENTORY_NBT_KEY, InventoryType.MINIGAME.toString());

    public static final Integer LOBBY_PER_PAGE = 27;


}
