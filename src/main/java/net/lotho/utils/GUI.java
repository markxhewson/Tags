package net.lotho.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GUI {

    public static ItemStack createItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Chat.color(name));
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItemShort(final Material material, int id, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material);
        item.setDurability((short) id);

        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Chat.color(name));
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);
        return item;
    }
}
