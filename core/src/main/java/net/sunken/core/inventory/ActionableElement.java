package net.sunken.core.inventory;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class ActionableElement extends Element {

    @Getter
    private final Action action;
    @Getter
    private final UIRunnable runnable;

    public ActionableElement(Action action, ItemStack item, UIRunnable runnable) {
        super(item);
        this.action = action;
        this.runnable = runnable;
    }
}
