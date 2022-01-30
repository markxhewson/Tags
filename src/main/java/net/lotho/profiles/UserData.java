package net.lotho.profiles;

import lombok.Getter;
import lombok.Setter;
import net.lotho.Azazel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class UserData {
    private final Azazel instance = Azazel.getInstance();

    private UUID uuid;
    private Integer tokens;
    private String activeTag;

    public UserData(UUID uuid, String activeTag, Integer tokens) {
        this.uuid = uuid;
        this.activeTag = activeTag;
        this.tokens = tokens;
    }

    public void load(Player player) {
        this.instance.getMySQLManager().select("SELECT * FROM players WHERE uuid=?", resultSet -> {
            try {
                if(resultSet.next()) {
                    this.activeTag = resultSet.getString("activeTag");
                    this.tokens = resultSet.getInt("tokens");
                }
                else this.instance.getMySQLManager().execute("INSERT INTO players(uuid, activeTag, tokens) VALUES (?,?,?)", player.getUniqueId().toString(), null, 0);
            } catch(SQLException e) {
                Bukkit.getConsoleSender().sendMessage(e.getMessage());
            }
        }, player.getUniqueId().toString());
    }

    public void save(Player player) {
        this.instance.getMySQLManager().execute("UPDATE players SET activeTag=?, tokens=? WHERE uuid=?", activeTag, tokens, player.getUniqueId().toString());
    }
}
