package net.sunken.core.util;

import net.minecraft.server.v1_13_R1.NBTTagCompound;
import net.minecraft.server.v1_13_R1.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullUtil {

    public static ItemStack addTexture(ItemStack item, String id, String texture){
        net.minecraft.server.v1_13_R1.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.hasTag() ? stack.getTag() : new NBTTagCompound();

        NBTTagList textures = new NBTTagList();
        textures.add(new NBTTagCompound());
        textures.getCompound(0).setString("Value", texture);

        NBTTagCompound properties = new NBTTagCompound();
        properties.set("textures", textures);

        NBTTagCompound skullowner = new NBTTagCompound();
        skullowner.setString("Id", id);
        skullowner.set("Properties", properties);

        tag.set("SkullOwner", skullowner);

        stack.setTag(tag);
        item = CraftItemStack.asBukkitCopy(stack);
        return item;
    }

}
