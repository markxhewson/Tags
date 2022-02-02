package net.lotho.interfaces;

import net.lotho.Azazel;
import net.lotho.profiles.User;
import net.lotho.utils.Chat;
import net.lotho.utils.GUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class TagsMenu {
    private final Azazel instance;

    public String inventoryName;
    private final Inventory inventory;

    public TagsMenu(Azazel instance) {
        this.instance = instance;

        inventoryName = "» Your Tags";
        inventory = this.instance.getServer().createInventory(null, 54, inventoryName);
    }

    public void open(Player player) {
        setupItems(player);

        player.openInventory(this.inventory);
    }

    private void setupItems(Player player) {
        this.inventory.clear();

        ArrayList<String> playerTags = this.instance.tags.fetchTags(player.getUniqueId());

        this.inventory.setItem(45, GUI.createItem(Material.ARROW, "&7&lBack", Chat.color("&7Right click to go back to the tags menu.")));

        for (int i = 0; i < this.inventory.getSize(); i++) {
            if (i < 9) this.inventory.setItem(i, GUI.createItemShort(Material.STAINED_GLASS_PANE, 7, " "));
        }
        for (int i = 46; i < this.inventory.getSize(); i++) {
            if (this.inventory.getItem(i) == null) this.inventory.setItem(i, GUI.createItemShort(Material.STAINED_GLASS_PANE, 7, " "));
        }

        if (playerTags.size() < 1) {
            this.inventory.setItem(22, GUI.createItemShort(Material.INK_SACK, 1, "&cYou do not have any tags!", Chat.color("&7You can aquire tokens to create"), Chat.color("&7tags to show off to your friends!")));
        } else {
            this.inventory.setItem(22, null);
            this.inventory.setItem(53, GUI.createItem(Material.BARRIER, "&c&lDisable Tag", Chat.color("&7Right click to disable"), Chat.color("&7your current tag.")));

            for (int i = 0; i < playerTags.size(); i++) {
                int index = i +9;
                if (this.inventory.getItem(index) == null) {
                    this.inventory.setItem(index, GUI.createItem(Material.NAME_TAG,"&a» " + playerTags.get(i), Chat.color("&7Right click to activate!")));
                }
            }
        }
    }

    public void handleClick(Inventory inventory, Player clicker, ItemStack clickedItem, int slot) {
        switch (clickedItem.getType()) {
            case NAME_TAG:
                if (slot > 8) { // checks if tag is valid and not a filler item in gui
                    String tagName = clickedItem.getItemMeta().getDisplayName().split(" ")[1];

                    User user = this.instance.getUserManager().getUser(clicker);
                    user.getData().setActiveTag(tagName);

                    clicker.sendMessage(Chat.color("&a&lSuccess! &7You have successfully activated the &c" + tagName + "&r &7tag!"));
                    clicker.closeInventory();
                }
                break;

            case ARROW:
                clicker.closeInventory();

                TokensMenu tokensMenu = (TokensMenu) this.instance.playerInterfaces.get(clicker.getUniqueId()).get("tokensMenu");
                tokensMenu.open(clicker);
                break;

            case BARRIER:
                User user = this.instance.getUserManager().getUser(clicker);
                if (user.getData().getActiveTag() == null) {
                    clicker.sendMessage(Chat.color("&c&lError! &7You do not have a tag activated."));
                } else {
                    user.getData().setActiveTag(null);
                    clicker.sendMessage(Chat.color("&a&lSuccess! &7You have disabled your current tag!"));
                }

                clicker.closeInventory();
        }
    }


}
