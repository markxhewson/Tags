package net.lotho.utils.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.lotho.Azazel;
import net.lotho.profiles.User;
import org.bukkit.OfflinePlayer;

public class Placeholders extends PlaceholderExpansion {

    private final Azazel instance;

    public Placeholders(Azazel instance) {
        this.instance = instance;
    }

    @Override
    public String getIdentifier() {
        return "azazel";
    }

    @Override
    public String getAuthor() {
        return "Lotho";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        User user = this.instance.getUserManager().getUser(player);

        switch (params.toLowerCase()) {
            case "tag":
                return user.getData().getActiveTag() == null ? "" : user.getData().getActiveTag();

            case "tokens":
                return user.getData().getTokens().toString();

            default:
                return null;
        }
    }
}
