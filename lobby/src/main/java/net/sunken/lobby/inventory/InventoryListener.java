package net.sunken.lobby.inventory;

import net.sunken.core.util.nbt.NBTItem;
import net.sunken.lobby.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack clicked = event.getItem();

        if (clicked != null) {
            NBTItem nbtItem = new NBTItem(clicked);

            if (nbtItem.getKeys().contains(Constants.INVENTORY_NBT_KEY)) {
                InventoryType type = InventoryType.valueOf(nbtItem.getString(Constants.INVENTORY_NBT_KEY));

                switch (type) {
                    case LOBBY:
                        LobbyInventory.getContainer().launchFor(player);
                        break;
                    case MINIGAME:
                        break;
                }

            }
        }
    }

}
