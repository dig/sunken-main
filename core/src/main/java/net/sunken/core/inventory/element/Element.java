package net.sunken.core.inventory.element;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import net.sunken.core.util.nbt.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Element {

    static final String ELEMENT_NBT_KEY = "SunkenElement";

    @Getter
    private static final Cache<UUID, Element> elementRegistry;

    static {
        elementRegistry = CacheBuilder.newBuilder()
                                      .build();
    }

    @Getter
    protected ItemStack item;

    public Element(ItemStack item) {
        this.item = item;

        UUID elementUUID = UUID.randomUUID();
        NBTItem nbtItem = new NBTItem(this.item);
        nbtItem.setString(ELEMENT_NBT_KEY, elementUUID.toString());
        this.item = nbtItem.getItem();
        elementRegistry.put(elementUUID, this);
    }
}
