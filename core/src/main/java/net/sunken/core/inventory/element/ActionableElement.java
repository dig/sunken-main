package net.sunken.core.inventory.element;

import lombok.Getter;
import net.sunken.core.inventory.runnable.UIRunnable;
import org.bukkit.inventory.ItemStack;

public class ActionableElement extends Element {

    @Getter
    private final UIRunnable runnable;

    @Getter
    private Action action;

    public ActionableElement(ItemStack item, UIRunnable runnable) {
        super(item);
        this.runnable = runnable;
        this.action = Action.CLICK;
    }

    public ActionableElement(ItemStack item, Action action, UIRunnable runnable) {
        this(item, runnable);
        this.action = action;
    }
}
