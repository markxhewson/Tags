package net.lotho.interfaces;

import lombok.Getter;
import net.lotho.Azazel;
import net.lotho.profiles.User;
import net.lotho.utils.Chat;
import net.lotho.utils.GUI;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

@Getter
public class TokensMenu {
    private final Azazel instance;

    public String inventoryName;
    private final Inventory inventory;

    public TokensMenu(Azazel instance) {
        this.instance = instance;

        inventoryName = "» Tokens Menu";
        inventory = this.instance.getServer().createInventory(null, 27, this.inventoryName);

        setupItems();
    }

    public void open(final Player player) {
        User user = this.instance.getUserManager().getUser(player);
        Integer tokens = user.getData().getTokens();

        this.inventory.setItem(11, GUI.createItem(Material.YELLOW_FLOWER, "&e&lYour Tokens", Chat.color("&7You have &e" + tokens + " &7tokens!")));

        if (player.hasPermission("manager.functions")) {
            this.inventory.setItem(8, GUI.createItem(Material.BARRIER, "&c&lManager Functions", Chat.color("&7Right click to view accessible"), Chat.color("&7administrative functions.")));
        } else {
            this.inventory.setItem(8, GUI.createItemShort(Material.STAINED_GLASS_PANE, 7, " "));
        }
        player.openInventory(this.inventory);
    }

    private void setupItems() {
        this.inventory.clear();

        this.inventory.setItem(13, GUI.createItem(Material.WATCH, "&6&lCreate a Tag", Chat.color("&7Right click to start creation"), Chat.color("&7of your very own tag!")));
        this.inventory.setItem(15, GUI.createItem(Material.NAME_TAG, "&a&lYour Tags", Chat.color("&7Right click to view all the"), Chat.color("&7tags you currently own.")));

        for (int i = 0; i < this.inventory.getSize(); i++) {
            if (this.inventory.getItem(i) == null || Objects.equals(this.inventory.getItem(i), new ItemStack(Material.AIR))) {
                this.inventory.setItem(i, GUI.createItemShort(Material.STAINED_GLASS_PANE, 7, " "));
            }
        }
    }

    public void handleClick(Inventory inventory, Player clicker, ItemStack clickedItem, int slot) {
        switch (clickedItem.getType()) {
            case WATCH: // create a tag
                clicker.closeInventory();

                User user = this.instance.getUserManager().getUser(clicker);
                if (user.getData().getTokens() > 0) {
                    new AnvilGUI.Builder()
                            .onComplete((player, text) -> {
                                this.instance.tags.tagValidationCheck(text, clicker);

                                return AnvilGUI.Response.close();
                            })
                            .text("» Tag Name")
                            .itemLeft(GUI.createItem(Material.NAME_TAG, "Tag"))
                            .plugin(this.instance)
                            .open(clicker);
                } else {
                    clicker.sendMessage(Chat.color("&c&lError! &7You do not have enough tokens to create a tag."));
                    clicker.closeInventory();
                }
                break;

            case NAME_TAG: // view your tags
                clicker.closeInventory();


                TagsMenu tagsMenu = (TagsMenu) this.instance.playerInterfaces.get(clicker.getUniqueId()).get("tagsMenu");
                tagsMenu.open(clicker);
                break;

            case BARRIER: // manager functions for tags
                clicker.closeInventory();

                ManagerMenu managerMenu = (ManagerMenu) this.instance.playerInterfaces.get(clicker.getUniqueId()).get("managerMenu");
                managerMenu.open(clicker);

                break;

        }
    }
}
