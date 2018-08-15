package net.sunken.core.inventory.element;

import lombok.Getter;
import net.sunken.core.inventory.runnable.UIRunnable;
import org.bukkit.inventory.ItemStack;

public class ActionableElement extends Element {

    public static final String ACTIONABLE_NBT_KEY = "SunkenActionable";

    @Getter
    private final UIRunnable runnable;

    public ActionableElement(ItemStack item, UIRunnable runnable) {
        super(item);
        this.runnable = runnable;
    }
}
