package net.sunken.core.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemFactory {

    public static ItemStack createItemStack(Material type, String name, List<String> lore){
        ItemStack result = new ItemStack(type, 1);
        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> loreColor = new ArrayList<>();
        for(String s : lore){
            loreColor.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        meta.setLore(loreColor);

        result.setItemMeta(meta);
        return result;
    }

    public static ItemStack createItemStack(Material type, String name){
        return createItemStack(type, name, new ArrayList<>());
    }

    public static ItemStack createItemStack(ItemStack item, String name, List<String> lore){
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> loreColor = new ArrayList<>();
        for(String s : lore){
            loreColor.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        meta.setLore(loreColor);

        item.setItemMeta(meta);
        return item;
    }

}
