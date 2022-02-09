package net.lotho;

import lombok.Getter;
import net.lotho.commands.TagsCMD;
import net.lotho.commands.TokensCMD;
import net.lotho.configs.ConfigManager;
import net.lotho.interfaces.ManagerMenu;
import net.lotho.interfaces.ManagerTagMenu;
import net.lotho.interfaces.TagsMenu;
import net.lotho.interfaces.TokensMenu;
import net.lotho.listeners.AsyncChatListener;
import net.lotho.listeners.InventoryListener;
import net.lotho.listeners.UpdateUser;
import net.lotho.profiles.User;
import net.lotho.profiles.UserManager;
import net.lotho.sql.MySQLManager;
import net.lotho.utils.api.Mojang;
import net.lotho.utils.api.Placeholders;
import net.lotho.utils.database.Tags;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

@Getter
public final class Azazel extends JavaPlugin {

    public HashMap<String, UUID> playerUUIDs = new HashMap<>();
    public HashMap<UUID, HashMap<String, Object>> playerInterfaces = new HashMap<>();

    @Getter
    public static Azazel instance;
    public ConfigManager configManager;

    public MySQLManager mySQLManager;
    public UserManager userManager;

    public Tags tags = new Tags(this);
    public Mojang mojang = new Mojang(this);

    @Override
    public void onEnable() {
        super.onEnable();

        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.getServer().getConsoleSender().sendMessage("PlaceholderAPI Placeholders enabled.");
            new Placeholders(this).register();
        } else {
            this.getServer().getConsoleSender().sendMessage("Nah");
        }

        instance = this;
        configManager = new ConfigManager(this);

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new UpdateUser(), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new AsyncChatListener(), this);

        mySQLManager = new MySQLManager(this);
        userManager = new UserManager(this);

        this.getCommand("tags").setExecutor(new TagsCMD());
        this.getCommand("tokens").setExecutor(new TokensCMD());

        for (Player online : this.getServer().getOnlinePlayers()) {
            this.userManager.handleProfileCreation(online.getUniqueId(), 0);

            User user = this.getUserManager().getUser(online);

            HashMap<String, Object> interfaces = new HashMap<>();
            interfaces.put("managerMenu", new ManagerMenu(this));
            interfaces.put("managerTagMenu", new ManagerTagMenu(this));
            interfaces.put("tagsMenu", new TagsMenu(this));
            interfaces.put("tokensMenu", new TokensMenu(this));

            this.playerInterfaces.put(online.getUniqueId(), interfaces);


            try {
                this.getServer().getScheduler().runTaskAsynchronously(this, () -> user.getData().load(online));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDisable() {
        super.onDisable();

        mySQLManager.disconnect();
    }
}
