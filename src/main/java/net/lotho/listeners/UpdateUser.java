package net.lotho.listeners;

import net.lotho.Azazel;
import net.lotho.interfaces.ManagerMenu;
import net.lotho.interfaces.ManagerTagMenu;
import net.lotho.interfaces.TagsMenu;
import net.lotho.interfaces.TokensMenu;
import net.lotho.profiles.User;
import net.lotho.utils.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class UpdateUser implements Listener {

    private final Azazel instance = Azazel.getInstance();

    @EventHandler
    public void onPlayerLoad(AsyncPlayerPreLoginEvent event) {
        try {
            this.instance.userManager.handleProfileCreation(event.getUniqueId(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = this.instance.getUserManager().getUser(player);

        HashMap<String, Object> interfaces = new HashMap<>();
        interfaces.put("managerMenu", new ManagerMenu(this.instance));
        interfaces.put("managerTagMenu", new ManagerTagMenu(this.instance));
        interfaces.put("tagsMenu", new TagsMenu(this.instance));
        interfaces.put("tokensMenu", new TokensMenu(this.instance));

        this.instance.playerInterfaces.put(player.getUniqueId(), interfaces);

        try {
            this.instance.getServer().getScheduler().runTaskAsynchronously(this.instance, () -> user.getData().load(player));
        } catch (NullPointerException e) {
            player.kickPlayer("ERROR: Profile could not be loaded.");
        }
    }

    @EventHandler
    public void saveData(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = this.instance.getUserManager().getUser(player);

        this.instance.playerInterfaces.remove(player.getUniqueId());

        if (user != null) {
            this.instance.getServer().getScheduler().runTaskAsynchronously(this.instance, () -> user.getData().save(player));
        }
    }
}
