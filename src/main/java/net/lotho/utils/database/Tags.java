package net.lotho.utils.database;

import net.lotho.Azazel;
import net.lotho.profiles.User;
import net.lotho.utils.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Tags {

    private final Azazel instance;

    public Tags(Azazel instance) {
        this.instance = instance;
    }

    public boolean checkUser(final UUID uuid) {
        boolean[] exists = new boolean[1];

        this.instance.getMySQLManager().select("SELECT uuid FROM players WHERE uuid=?", resultSet -> {
            try {
                if (resultSet.next()) {
                    if (resultSet.getString("uuid") != null) exists[0] = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, uuid.toString());

        try {
            Thread.sleep(35);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return exists[0];
    }

    public ArrayList<String> fetchTags(final UUID uuid) {
        ArrayList<String> tags = new ArrayList<>();

        this.instance.getMySQLManager().select("SELECT id, name FROM tags WHERE ownerUUID=?", resultSet -> {
            try {
                while (resultSet.next()) {
                    tags.add(resultSet.getString("name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, uuid.toString());

        try {
            Thread.sleep(35);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return tags;
    }

    public void adminDisableTag(final UUID uuid) {
        this.instance.getMySQLManager().execute("UPDATE players SET activeTag=? WHERE uuid=?", null, uuid.toString());
    }

    public void blockTag(String name, Player player) {
        ArrayList<String> words = (ArrayList<String>) this.instance.configManager.getConfigFile().getStringList("tags.blockedTags");

        if (words.stream().anyMatch(t -> t.contains(name))) {
            player.sendMessage(Chat.color("&c&lError! &7You are unable to enter duplicate values into the blocked tags list."));
        } else {
            words.add(name.toLowerCase());

            this.instance.configManager.getConfigFile().set("tags.blockedTags", words);
            this.instance.configManager.saveConfigFile();

            player.sendMessage(Chat.color("&a&lSuccess! &7You have blocked the &c" + name + " &7tag from being used!"));
        }
    }

    public void deleteTag(String name, UUID uuid) {
        String[] tagToDelete = new String[1];

        this.instance.getMySQLManager().select("SELECT name FROM tags WHERE ownerUUID=?", resultSet -> {
            try {
                while (resultSet.next()) {
                    if (ChatColor.stripColor(Chat.color(resultSet.getString("name"))).equals(name)) {
                        tagToDelete[0] = resultSet.getString("name");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, uuid.toString());

        try {
            Thread.sleep(35);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.instance.getMySQLManager().execute("DELETE FROM tags WHERE ownerUUID=? AND name=?", uuid.toString(), tagToDelete[0]);
    }

    public boolean isTagBlocked(String name) {
        ArrayList<String> words = (ArrayList<String>) this.instance.configManager.getConfigFile().getStringList("tags.blockedTags");
        boolean[] blocked = new boolean[1];

        if (words.stream().anyMatch(name::contains)) {
            blocked[0] = true;
        }

        return blocked[0];
    }

    public String adminGetActiveTag(final UUID uuid) {
        String[] activeTag = new String[1];

        this.instance.getMySQLManager().select("SELECT name FROM players WHERE uuid=?", resultSet -> {
            try {
                if (resultSet.next()) {
                    activeTag[0] = resultSet.getString("name");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, uuid.toString());

        return activeTag[0];
    }

    public void createTag(String name, Player owner) {
        this.instance.getMySQLManager().select("SELECT max(id) FROM tags", resultSet -> {
            try {
                if (resultSet.next()) {
                    int tagCount = resultSet.getInt(1);
                    this.instance.getMySQLManager().execute("INSERT INTO tags (id, name, ownerUUID) VALUES (?,?,?)", tagCount +1, name, owner.getUniqueId().toString());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void tagValidationCheck(String name, Player owner) {
        User user = this.instance.getUserManager().getUser(owner);

        if (name.length() < 3) {
            owner.sendMessage(Chat.color("&c&lError! &7You tag is too short, it must be greater than two characters."));
        } else {
            if (name.length() > 15) {
                owner.sendMessage(Chat.color("&c&lError! &7Your tag is too long! It must be less than 15 characters."));
            } else {
                if (!checkTagExists(name)) {
                    boolean isTagBlocked = this.instance.tags.isTagBlocked(name);

                    if (!isTagBlocked) {
                        user.getData().setTokens(user.getData().getTokens() -1);
                        owner.sendMessage(Chat.color("&a&lSuccess! &7You have created the &c" + name + " &7tag!"));

                        createTag(name, owner);
                    } else {
                        owner.sendMessage(Chat.color("&c&lError! &7That tag has been blocked by server administrators."));
                    }
                } else {
                    owner.sendMessage(Chat.color("&c&lError! &7You already have a tag with that name!"));
                }
            }
        }
    }

    public boolean checkTagExists(String name) {
        final boolean[] exists = new boolean[1];

        this.instance.getMySQLManager().select("SELECT name FROM tags WHERE name=?", resultSet -> {
            try {
                if (resultSet.next()) {
                    if (resultSet.getString("name") != null) exists[0] = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, name.toLowerCase());

        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return exists[0];
    }

}
