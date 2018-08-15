package net.sunken.core.inventory.element;

import com.google.common.cache.Cache;
import net.sunken.core.inventory.PageContainer;
import net.sunken.core.inventory.runnable.UIRunnableContext;
import net.sunken.core.util.nbt.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ElementClickListener implements Listener {

    private static final Cache<UUID, ActionableElement> actionableElements = PageContainer.getActionableElements();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();

        if (clicked != null) {
            NBTItem nbtItem = new NBTItem(clicked);
            
            if (nbtItem.hasKey(ActionableElement.ACTIONABLE_NBT_KEY)) {
                String uuid = nbtItem.getString(ActionableElement.ACTIONABLE_NBT_KEY);
                ActionableElement actionableElement = actionableElements.getIfPresent(UUID.fromString(uuid));
                if (actionableElement != null) {
                    actionableElement.getRunnable().run(new UIRunnableContext(player));
                }
            }
        }
    }
}
