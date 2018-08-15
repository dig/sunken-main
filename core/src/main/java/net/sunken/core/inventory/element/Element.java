package net.sunken.core.inventory.element;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class Element {

    @Getter
    protected ItemStack item;

    public Element(ItemStack item) {
        this.item = item;
    }
}
