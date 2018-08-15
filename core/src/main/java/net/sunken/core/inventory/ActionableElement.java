package net.sunken.core.inventory;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class ActionableElement extends Element {

    @Getter
    private final Action action;

    public ActionableElement(Action action, ItemStack item) {
        super(item);
        this.action = action;
    }
}
