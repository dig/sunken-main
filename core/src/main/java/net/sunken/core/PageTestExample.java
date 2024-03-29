package net.sunken.core;

import net.sunken.core.inventory.Page;
import net.sunken.core.inventory.PageContainer;
import net.sunken.core.inventory.element.ActionableElement;
import net.sunken.core.inventory.element.Element;
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
                                .putElement(1, new ActionableElement(new ItemStack(Material.ACACIA_BUTTON), context -> {
                                    Player observer = context.getObserver();
                                    observer.sendMessage("yo");
                                    return context;
                                }))
                                .build();
        // add the page to the state container
        container.addPage(mainMenuPage);

        Page anotherPage = Page.builder("another-menu")
                                .title("Some other shit page")
                                .size(54)
                                .putElement(0, new Element(new ItemStack(Material.BEDROCK)))
                                .putElement(1, new ActionableElement(new ItemStack(Material.BIRCH_BOAT), context -> {
                                    Player observer = context.getObserver();
                                    observer.sendMessage("another page");
                                    container.openPage(observer, "main-menu");
                                    return context;
                                }))
                                .build();
        container.addPage(anotherPage);

        // set it as the page to launch with
        container.setInitialPage(anotherPage);

        // launch
        container.launchFor(player);
    }
}
