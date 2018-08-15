package net.sunken.core.inventory.element;

import lombok.Getter;
import net.sunken.core.inventory.PageContainer;
import net.sunken.core.inventory.runnable.UIRunnable;
import net.sunken.core.util.nbt.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ActionableElement extends Element {

    @Getter
    private final UIRunnable runnable;

    public ActionableElement(ItemStack item, UIRunnable runnable) {
        super(item);
        this.runnable = runnable;
    }
}
