package net.sunken.core.inventory;

import com.google.common.collect.Maps;
import lombok.Getter;
import net.sunken.core.inventory.element.Element;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

public class PageContainer {

    @Getter
    private final Map<String, Page> pages;

    @Getter
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
        page.updateInventory();

        player.openInventory(page.getInventory());
    }
}
