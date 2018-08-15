package net.sunken.core.inventory.element;

import net.sunken.core.inventory.runnable.UIRunnableContext;
import net.sunken.core.util.nbt.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ElementClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();

        if (clicked != null) {
            NBTItem nbtItem = new NBTItem(clicked);
            if (nbtItem.getKeys().contains(Element.ELEMENT_NBT_KEY)) {
                String uuid = nbtItem.getString(Element.ELEMENT_NBT_KEY);
                Element element = Element.getElementRegistry().getIfPresent(UUID.fromString(uuid));

                if (element != null) {
                    e.setCancelled(true); // cancel by default since it's a GUI

                    if (element instanceof ActionableElement) {
                        UIRunnableContext context = new UIRunnableContext(player);
                        context.setCancelled(e.isCancelled());

                        UIRunnableContext modifiedContext = ((ActionableElement) element).getRunnable().run(context);
                        e.setCancelled(modifiedContext.isCancelled());
                    }
                }
            }
        }
    }
}
