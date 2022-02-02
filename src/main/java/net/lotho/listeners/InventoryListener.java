package net.lotho.listeners;

import net.lotho.Azazel;
import net.lotho.interfaces.ManagerMenu;
import net.lotho.interfaces.ManagerTagMenu;
import net.lotho.interfaces.TagsMenu;
import net.lotho.interfaces.TokensMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {

    private final Azazel instance = Azazel.getInstance();

    @EventHandler
    public void inventoryListen(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();

        TokensMenu tokensMenu = (TokensMenu) this.instance.playerInterfaces.get(e.getWhoClicked().getUniqueId()).get("tokensMenu");
        TagsMenu tagsMenu = (TagsMenu) this.instance.playerInterfaces.get(e.getWhoClicked().getUniqueId()).get("tagsMenu");
        ManagerMenu managerMenu = (ManagerMenu) this.instance.playerInterfaces.get(e.getWhoClicked().getUniqueId()).get("managerMenu");
        ManagerTagMenu managerTagMenu = (ManagerTagMenu) this.instance.playerInterfaces.get(e.getWhoClicked().getUniqueId()).get("managerTagMenu");

        if (tokensMenu.inventoryName.equals(inventory.getName()) && e.getCurrentItem() != null) {
            tokensMenu.handleClick(inventory, (Player) e.getWhoClicked(), e.getCurrentItem(), e.getSlot());

            e.setCancelled(true);
        }
        else if (tagsMenu.inventoryName.equals(inventory.getName()) && e.getCurrentItem() != null) {
            tagsMenu.handleClick(inventory, (Player) e.getWhoClicked(), e.getCurrentItem(), e.getSlot());

            e.setCancelled(true);
        }
        else if (managerMenu.inventoryName.equals(inventory.getName()) && e.getCurrentItem() != null) {
            managerMenu.handleClick(inventory, (Player) e.getWhoClicked(), e.getCurrentItem(), e.getSlot());

            e.setCancelled(true);
        }
        else if (managerTagMenu.inventoryName.equals(inventory.getName()) && e.getCurrentItem() != null) {
            managerTagMenu.handleClick(inventory, (Player) e.getWhoClicked(), e.getCurrentItem(), e.getSlot());

            e.setCancelled(true);
        }
    }
}
