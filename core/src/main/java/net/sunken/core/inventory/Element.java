package net.sunken.core.inventory;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class Element {

    @Getter
    private final ItemStack item;

    public Element(ItemStack item) {
        this.item = item;
    }
}
