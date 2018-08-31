package net.sunken.lobby.inventory;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.ServerInstance;
import net.sunken.common.event.ListensToEvent;
import net.sunken.common.event.SunkenListener;
import net.sunken.common.server.data.ServerObject;
import net.sunken.common.server.event.ServerCacheUpdateEvent;
import net.sunken.core.inventory.Page;
import net.sunken.core.inventory.PageContainer;
import net.sunken.core.inventory.element.ActionableElement;
import net.sunken.core.inventory.element.Element;
import net.sunken.core.util.BungeeUtil;
import net.sunken.core.util.ItemFactory;
import net.sunken.lobby.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LobbyInventory implements SunkenListener {

    @Getter
    private static PageContainer container;

    static {
        container = new PageContainer();
    }

    @ListensToEvent
    public void onServerCacheUpdate(ServerCacheUpdateEvent event) {
        ServerObject currentServer = ServerInstance.instance().getServerObject();

        // Clear all elements
        for (Page page : container.getPages().values()) {
            page.clearElements();
        }

        int index = 0;
        for (ServerObject server : Common.getInstance().getServerCache().getCache(currentServer.getServerType())) {
            int page = (int) Math.floor((index / Constants.LOBBY_PER_PAGE) + 1);

            Element element = new ActionableElement(this.createServerItem(server), context -> {
                Player observer = context.getObserver();
                BungeeUtil.sendPlayerToServer(observer, server.getServerName());
                return context;
            });

            // Checking if the page already exist, if so update current page.
            if (container.getPages() != null && container.getPages().size() >= page) {
                Page currentPage = null;

                int i = 1;
                for (Page cur : container.getPages().values()) {
                    if (i == page) {
                        currentPage = cur;
                        break;
                    }

                    i++;
                }

                // Add new element if page is found
                if (currentPage != null) {
                    currentPage.getElements().put(index, element);

                    // Once we have updated the elements, call to rebuild the inventory.
                    currentPage.updateInventory();
                }
            } else {
                Page.Builder newPage = Page.builder("page-" + page)
                        .title(currentServer.getServerType().getFriendlyName() + " Selector")
                        .size(Constants.LOBBY_PER_PAGE);

                newPage.putElement(index, element);

                Page finalPage = newPage.build();
                container.addPage(finalPage);

                if (container.getInitialPage() == null) {
                    container.setInitialPage(finalPage);
                }
            }

            index++;
        }
    }

    private ItemStack createServerItem(ServerObject server) {
        String name = ChatColor.GREEN + server.getServerType().getFriendlyName();
        Material material = Material.GREEN_CONCRETE;

        if (ServerInstance.instance().getServerObject().equals(server)) {
            name = ChatColor.RED + server.getServerType().getFriendlyName();
            material = Material.YELLOW_CONCRETE;
        } else if (server.getPlayerCount() >= server.getMaxPlayers()) {
            name = ChatColor.RED + server.getServerType().getFriendlyName();
            material = Material.RED_CONCRETE;
        }

        name += " #" + Common.getInstance().getServerCache().getServerNumber(server);

        List<String> lores = new ArrayList<>();
        lores.add(ChatColor.GRAY + "Players: " + server.getPlayerCount() + "/" + server.getMaxPlayers());
        lores.add(" ");

        if (ServerInstance.instance().getServerObject().equals(server)) {
            lores.add(ChatColor.RED + "CONNECTED");
        } else {
            lores.add(ChatColor.YELLOW + "> Click to connect!");
        }

        return ItemFactory.createItemStack(material, name, lores);
    }

}
