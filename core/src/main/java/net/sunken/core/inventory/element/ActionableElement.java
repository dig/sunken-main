package net.sunken.core.inventory.element;

import lombok.Getter;
import net.sunken.core.inventory.PageContainer;
import net.sunken.core.inventory.runnable.UIRunnable;
import net.sunken.core.util.nbt.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ActionableElement extends Element {

    static final String ACTIONABLE_NBT_KEY = "SunkenActionable";

    @Getter
    private final UIRunnable runnable;

    public ActionableElement(ItemStack item, UIRunnable runnable) {
        super(item);
        this.runnable = runnable;

        UUID actionableElementUUID = UUID.randomUUID();
        NBTItem nbtItem = new NBTItem(this.item);
        nbtItem.setString(ACTIONABLE_NBT_KEY, actionableElementUUID.toString());
        this.item = nbtItem.getItem();
        PageContainer.getActionableElements().put(actionableElementUUID, this);
    }
}
