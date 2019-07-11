package net.sunken.core.commands.servers;

import net.sunken.common.Common;
import net.sunken.common.ServerInstance;
import net.sunken.common.event.ListensToEvent;
import net.sunken.common.event.SunkenListener;
import net.sunken.common.server.data.ServerObject;
import net.sunken.common.server.event.ServerCacheUpdateEvent;
import net.sunken.common.type.ServerType;
import net.sunken.common.util.TimeHelper;
import net.sunken.core.inventory.Page;
import net.sunken.core.inventory.PageContainer;
import net.sunken.core.inventory.element.ActionableElement;
import net.sunken.core.inventory.element.Element;
import net.sunken.core.model.ModelCommand;
import net.sunken.core.util.BungeeUtil;
import net.sunken.core.util.ItemFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ServersListener implements SunkenListener {

    private static List<Material> materialList = Arrays.asList(
            Material.ORANGE_CONCRETE,
            Material.YELLOW_CONCRETE,
            Material.GREEN_CONCRETE,
            Material.BLUE_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.PINK_CONCRETE,
            Material.CYAN_CONCRETE,
            Material.RED_CONCRETE,
            Material.MAGENTA_CONCRETE,
            Material.LIME_CONCRETE,
            Material.BROWN_CONCRETE,
            Material.BLACK_CONCRETE,
            Material.GRAY_CONCRETE,
            Material.WHITE_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE,
            Material.LIGHT_GRAY_CONCRETE
    );

    @ListensToEvent
    public void onServerCacheUpdate(ServerCacheUpdateEvent event) {
        PageContainer container = ServersCommand.getContainer();

        // Clear all elements
        for (Page page : container.getPages().values()) {
            page.clearElements();
        }

        int index = 0;
        for (ServerType type : ServerType.values()) {
            for (ServerObject server : Common.getInstance().getServerCache().getCache(type)) {
                int page = (int) Math.floor((index / 54) + 1);

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
                            .title("Servers")
                            .size(54);

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
    }

    private ItemStack createServerItem(ServerObject server) {
        String name = ChatColor.GREEN + server.getServerType().getFriendlyName();
        Material material = materialList.get(server.getServerType().ordinal());

        if (server.getPlayerCount() >= server.getMaxPlayers()
                || ServerInstance.instance().getServerObject().equals(server)) {
            name = ChatColor.RED + server.getServerType().getFriendlyName();
        }

        TimeHelper timeHelper = new TimeHelper(System.currentTimeMillis() - server.getCreated());
        int serverNum = Common.getInstance().getServerCache().getServerNumber(server);
        name += " #" + serverNum;

        List<String> lores = new ArrayList<>();
        // lores.add(ChatColor.GRAY + "UUID: " + server.getServerName());
        lores.add(ChatColor.GRAY + "Type: " + server.getServerType().toString());
        lores.add(ChatColor.GRAY + "Players: " + server.getPlayerCount() + "/" + server.getMaxPlayers());
        lores.add(ChatColor.GRAY + "Uptime: " + timeHelper.getShortTime());
        lores.add(ChatColor.GRAY + "IP: " + server.getServerIp() + ":" + server.getServerPort());
        lores.add(" ");

        if (ServerInstance.instance().getServerObject().equals(server)) {
            lores.add(ChatColor.RED + "CONNECTED");
        } else {
            lores.add(ChatColor.YELLOW + "Click to connect!");
        }

        ItemStack item = ItemFactory.createItemStack(material, name, lores);
        item.setAmount(serverNum);

        return item;
    }

}
