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

    public TokensMenu tokensMenu = new TokensMenu(this.instance);
    public TagsMenu tagsMenu = new TagsMenu(this.instance);
    public ManagerMenu managerMenu = new ManagerMenu(this.instance);
    public ManagerTagMenu managerTagMenu = new ManagerTagMenu(this.instance);

    @EventHandler
    public void inventoryListen(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();

        if (this.tokensMenu.inventoryName.equals(inventory.getName()) && e.getCurrentItem() != null) {
            this.tokensMenu.handleClick(inventory, (Player) e.getWhoClicked(), e.getCurrentItem(), e.getSlot());

            e.setCancelled(true);
        }
        else if (this.tagsMenu.inventoryName.equals(inventory.getName()) && e.getCurrentItem() != null) {
            this.tagsMenu.handleClick(inventory, (Player) e.getWhoClicked(), e.getCurrentItem(), e.getSlot());

            e.setCancelled(true);
        }
        else if (this.managerMenu.inventoryName.equals(inventory.getName()) && e.getCurrentItem() != null) {
            this.managerMenu.handleClick(inventory, (Player) e.getWhoClicked(), e.getCurrentItem(), e.getSlot());

            e.setCancelled(true);
        }
        else if (this.managerTagMenu.inventoryName.equals(inventory.getName()) && e.getCurrentItem() != null) {
            this.managerTagMenu.handleClick(inventory, (Player) e.getWhoClicked(), e.getCurrentItem(), e.getSlot());

            e.setCancelled(true);
        }
    }
}
