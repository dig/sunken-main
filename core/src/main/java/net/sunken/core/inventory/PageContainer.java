package net.sunken.core.inventory;

import com.google.common.collect.Maps;
import net.sunken.core.inventory.element.Element;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

public class PageContainer {

    private final Map<String, Page> pages;

    private String initialPage;

    {
        pages = Maps.newHashMap();
    }

    public void addPage(Page page) {
        this.pages.put(page.getId(), page);
    }

    public void setInitialPage(Page page) {
        this.initialPage = page.getId();

        if (!this.pages.containsKey(page.getId())) {
            this.addPage(page);
        }
    }

    public void launchFor(Player player) {
        this.openPage(player, initialPage);
    }

    public void openPage(Player player, String pageId) {
        checkState(pages.containsKey(pageId), "there is no page that exists with that ID");

        // create an inventory to open from a page
        Page page = this.pages.get(pageId);
        Inventory inventory = Bukkit.createInventory(null, page.getSize(), page.getTitle());
        // add items to the inventory
        Map<Integer, Element> elements = page.getElements();
        elements.forEach((position, element) -> inventory.setItem(position, element.getItem()));

        player.openInventory(inventory);
    }
}
