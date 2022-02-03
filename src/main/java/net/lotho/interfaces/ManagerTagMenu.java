package net.lotho.interfaces;

import net.lotho.Azazel;
import net.lotho.profiles.User;
import net.lotho.utils.Chat;
import net.lotho.utils.GUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class ManagerTagMenu {
    private final Azazel instance;

    public String inventoryName;
    private final Inventory inventory;
    private UUID tagOwner;

    public ManagerTagMenu(Azazel instance) {
        this.instance = instance;
        this.tagOwner = null;

        inventoryName = "» Modify User Tags";
        inventory = this.instance.getServer().createInventory(null, 54, inventoryName);
    }

    public void open(Player player, HashMap<Integer, String> userTags, UUID tagOwner) {
        setupItems(userTags);

        this.tagOwner = tagOwner;
        player.openInventory(this.inventory);
    }

    public void setupItems(HashMap<Integer, String> playerTags) {
        this.inventory.clear();

        this.inventory.setItem(45, GUI.createItem(Material.ARROW, "&7&lBack", Chat.color("&7Right click to go back to the manager menu.")));

        for (int i = 0; i < this.inventory.getSize(); i++) {
            if (i < 9) this.inventory.setItem(i, GUI.createItemShort(Material.STAINED_GLASS_PANE, 7, " "));
        }
        for (int i = 46; i < this.inventory.getSize(); i++) {
            if (this.inventory.getItem(i) == null) this.inventory.setItem(i, GUI.createItemShort(Material.STAINED_GLASS_PANE, 7, " "));
        }

        if (playerTags.size() < 1) {
            this.inventory.setItem(22, GUI.createItemShort(Material.INK_SACK, 1, "&cThis user does not have any tags!", Chat.color("&7You are unable to modify nothing.")));
        } else {
            this.inventory.setItem(22, null);
            this.inventory.setItem(53, GUI.createItem(Material.BARRIER, "&c&lDisable User Tag", Chat.color("&7Right click to disable their"), Chat.color("&7current active tag.")));

            final int[] index = {9};
            playerTags.forEach((id, name) -> {
                if (this.inventory.getItem(index[0]) == null) {
                    this.inventory.setItem(index[0], GUI.createItem(Material.NAME_TAG,"&a» " + name + " &7(" + id.toString() +")", "", Chat.color("&c&o(Right click to revoke from user)")));
                }
                index[0]++;
            });
        }
    }

    public void handleClick(Inventory inventory, Player clicker, ItemStack clickedItem, int slot) {
        switch (clickedItem.getType()) {
            case NAME_TAG:
                if (slot > 8) { // checks if tag is valid and not a filler item in gui
                    String tagName = clickedItem.getItemMeta().getDisplayName().split(" ")[1];

                    this.instance.tags.deleteTag(ChatColor.stripColor(tagName), this.tagOwner);

                    User user = this.instance.getUserManager().getUser(this.tagOwner);

                    if (user == null) {
                        if (ChatColor.stripColor(Chat.color(this.instance.tags.adminGetActiveTag(this.tagOwner))).equals(ChatColor.stripColor(tagName))) {
                            this.instance.tags.adminDisableTag(this.tagOwner);
                        }
                    }
                    else {
                        if (user.getData().getActiveTag() != null) {
                            if (ChatColor.stripColor(Chat.color(user.getData().getActiveTag())).equals(ChatColor.stripColor(tagName))) user.getData().setActiveTag(null);
                        }
                    }

                    clicker.sendMessage(Chat.color("&a&lSuccess! &7You have &crevoked &7the &a" + tagName + " &7tag from user!"));
                    clicker.closeInventory();

                    this.instance.getServer().getScheduler().runTaskLaterAsynchronously(this.instance, () -> {
                        ManagerTagMenu managerTagMenu = (ManagerTagMenu) this.instance.playerInterfaces.get(clicker.getUniqueId()).get("managerTagMenu");
                        managerTagMenu.open(clicker, this.instance.tags.fetchTags(this.tagOwner), this.tagOwner);
                    }, 10);
                }
                break;

            case ARROW:
                clicker.closeInventory();

                ManagerMenu managerMenu = (ManagerMenu) this.instance.playerInterfaces.get(clicker.getUniqueId()).get("managerMenu");
                managerMenu.open(clicker);
                break;

            case BARRIER:
                User user = this.instance.getUserManager().getUser(this.tagOwner);

                if (user == null) this.instance.tags.adminDisableTag(this.tagOwner);
                else user.getData().setActiveTag(null);

                clicker.sendMessage(Chat.color("&a&lSuccess! &7You have disabled the users tag!"));
                clicker.closeInventory();
        }
    }

}
