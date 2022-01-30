package net.lotho.listeners;

import net.lotho.Azazel;
import net.lotho.profiles.User;
import net.lotho.utils.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncChatListener implements Listener {

    private final Azazel instance = Azazel.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User user = this.instance.getUserManager().getUser(player);

        if (user.getData().getActiveTag() != null) {
            event.setFormat(Chat.color("&e%s &a" + user.getData().getActiveTag() + "&r&7: &f%s"));
        } else {
            event.setFormat(Chat.color("&e%s&7: &f%s"));
        }
    }
}
