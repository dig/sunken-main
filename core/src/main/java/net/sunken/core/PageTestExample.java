package net.sunken.core;

import net.sunken.core.inventory.Element;
import net.sunken.core.inventory.Page;
import net.sunken.core.inventory.PageContainer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PageTestExample {

    public static void testInventoryFramework(Player player) {
        // the state container
        PageContainer container = new PageContainer();

        // main menu page
        Page mainMenuPage = Page.builder("main-menu")
                                .title("Main menu of some kind")
                                .size(54)
                                .putElement(0, new Element(new ItemStack(Material.BEACON)))
                                .build();
        // add the page to the state container
        container.addPage(mainMenuPage);

        // set it as the page to launch with
        container.setInitialPage(mainMenuPage);

        // launch
        container.launchFor(player);
    }
}
