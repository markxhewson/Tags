package net.lotho.profiles;

import net.lotho.Azazel;
import net.lotho.sql.Manager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager extends Manager {
    private Map<UUID, User> users = new HashMap<>();

    public UserManager(Azazel instance) {
        super(instance);
    }

    public void handleProfileCreation(UUID uuid, Integer tokens) {
        if (!this.users.containsKey(uuid)) {
            users.put(uuid, new User(uuid, null, tokens));
        }
    }

    public User getUser(Object object) {
        if (object instanceof Player) {
            Player target = (Player) object;
            if (!this.users.containsKey(target.getUniqueId())) {
                return null;
            }
            return users.get(target.getUniqueId());
        }
        if (object instanceof UUID) {
            UUID uuid = (UUID) object;
            if (!this.users.containsKey(uuid)) {
                return null;
            }
            return users.get(uuid);
        }
        return null;
    }

    public Map<UUID, User> getUsers() {
        return this.users;
    }
}
