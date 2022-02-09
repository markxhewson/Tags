package net.lotho.interfaces;

import com.mashape.unirest.http.exceptions.UnirestException;
import net.lotho.Azazel;
import net.lotho.utils.Chat;
import net.lotho.utils.GUI;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ManagerMenu {
    private final Azazel instance;

    public String inventoryName;
    private final Inventory inventory;

    public ManagerMenu(Azazel instance) {
        this.instance = instance;

        inventoryName = "» Manager Functions";
        inventory = this.instance.getServer().createInventory(null, 27, inventoryName);
    }

    public void open(Player player) {
        setupItems();

        player.openInventory(this.inventory);
    }

    private void setupItems() {
        this.inventory.clear();

        this.inventory.setItem(11, GUI.createItem(Material.NETHER_STAR, "&4&lCreate a Blocked Tag", Chat.color("&7Right click to add some text to"), Chat.color("&7be automatically blocked when a"), Chat.color("&7user tries to create a tag.")));
        this.inventory.setItem(13, GUI.createItem(Material.SIGN, "&3&lView Blocked Tags", Chat.color("&7Right click to view the entire"), Chat.color("&7list of blocked tag usages.")));
        this.inventory.setItem(15, GUI.createItem(Material.BEACON, "&2&lModify Player Tags", Chat.color("&7Right click to view and modify a"), Chat.color("&7players list of tags.")));

        this.inventory.setItem(18, GUI.createItem(Material.ARROW, "&7&lBack", Chat.color("&7Go back to the tags menu.")));

        for (int i = 0; i < this.inventory.getSize(); i++) {
            if (this.inventory.getItem(i) == null || Objects.equals(this.inventory.getItem(i), new ItemStack(Material.AIR))) {
                this.inventory.setItem(i, GUI.createItemShort(Material.STAINED_GLASS_PANE, 7, " "));
            }
        }
    }

    public void handleClick(Inventory inventory, Player clicker, ItemStack clickedItem, int slot) {
        switch (clickedItem.getType()) {
            case NETHER_STAR:
                new AnvilGUI.Builder()
                        .onComplete((player, text) -> {
                            this.instance.tags.blockTag(text.toLowerCase(), player);
                            return AnvilGUI.Response.close();
                        })
                        .text("» Tag to block")
                        .plugin(this.instance)
                        .open(clicker);
                break;

            case SIGN:
                ArrayList<String> arrayList = (ArrayList<String>) this.instance.getConfigManager().getConfigFile().getStringList("tags.blockedTags");

                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append(Chat.color("\n&c&lMANAGER FUNCTIONS\n"));
                arrayList.forEach(s -> stringBuilder.append(Chat.color("&c» &a" + s + "\n")));
                stringBuilder.append(Chat.color("\n "));

                clicker.sendMessage(stringBuilder.toString());
                clicker.closeInventory();
                break;

            case BEACON:
                new AnvilGUI.Builder()
                        .onComplete((player, text) -> {
                            UUID[] uuid = new UUID[1];
                            if (this.instance.playerUUIDs.get(text.toLowerCase()) == null) {
                                try {
                                    this.instance.mojang.fetchPlayerUUID(text.toLowerCase());
                                } catch (UnirestException e) {
                                    e.printStackTrace();
                                }

                                this.instance.getServer().getScheduler().runTaskLaterAsynchronously(this.instance, () -> {
                                    uuid[0] = this.instance.playerUUIDs.get(text.toLowerCase());

                                    if (uuid[0] != null) {
                                        boolean playerExists = this.instance.tags.checkUser(uuid[0]);
                                        if (playerExists) {
                                            ManagerTagMenu managerTagMenu = (ManagerTagMenu) this.instance.playerInterfaces.get(clicker.getUniqueId()).get("managerTagMenu");
                                            managerTagMenu.open(clicker, this.instance.tags.fetchTags(uuid[0]), uuid[0]);
                                            this.instance.playerInterfaces.get(clicker.getUniqueId()).put("managerTagMenu", managerTagMenu);
                                        } else {
                                            player.sendMessage(Chat.color("&c&lError! &7I could not find that player in the database."));
                                        }
                                    } else {
                                        player.sendMessage(Chat.color("&c&lError! &7I could not find a player with that username."));
                                    }
                                },25);
                            } else {
                                uuid[0] = this.instance.playerUUIDs.get(text.toLowerCase());

                                if (uuid[0] != null) {
                                    boolean playerExists = this.instance.tags.checkUser(uuid[0]);
                                    if (playerExists) {
                                        ManagerTagMenu managerTagMenu = (ManagerTagMenu) this.instance.playerInterfaces.get(clicker.getUniqueId()).get("managerTagMenu");
                                        managerTagMenu.open(clicker, this.instance.tags.fetchTags(uuid[0]), uuid[0]);
                                        this.instance.playerInterfaces.get(clicker.getUniqueId()).put("managerTagMenu", managerTagMenu);
                                    } else {
                                        player.sendMessage(Chat.color("&c&lError! &7I could not find that player in the database."));
                                    }
                                } else {
                                    player.sendMessage(Chat.color("&c&lError! &7I could not find a player with that username."));
                                }
                            }


                            return AnvilGUI.Response.close();
                        })
                        .text("» Username")
                        .plugin(this.instance)
                        .open(clicker);

                break;

            case ARROW:
                clicker.closeInventory();

                TokensMenu tokensMenu = (TokensMenu) this.instance.playerInterfaces.get(clicker.getUniqueId()).get("tokensMenu");
                tokensMenu.open(clicker);
                break;
        }
    }


}
