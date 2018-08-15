package net.sunken.core.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PageTestExample {

    public static void testInventoryFramework(Player player) {
        PageContainer container = new PageContainer();
        Page mainMenuPage = Page.builder("main-menu")
                                .title("Main menu of some kind")
                                .size(54)
                                .putElement(0, new Element(new ItemStack(Material.BEACON)))
                                .build();
        container.addPage(mainMenuPage);
        container.setInitialPage(mainMenuPage);
        container.launchFor(player);
    }
}
