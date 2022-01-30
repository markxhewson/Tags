package net.lotho.commands;

import net.lotho.Azazel;
import net.lotho.profiles.User;
import net.lotho.utils.Chat;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TokensCMD implements CommandExecutor {

    private final Azazel instance = Azazel.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("manager.functions")) {
            player.sendMessage(Chat.color("&c&lError! &7You do not have manager functions required to use this."));
            return false;
        }

        if (args.length < 3) {
            player.sendMessage(Chat.color("&c&lError! &7Usage: &o/tokens <action> <details> <amount>\n &8- &7action: give, remove\n &8- &7details: player\n &8- &7amount: integer"));
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "grant":
            case "give":
                try {
                    Player receiver = this.instance.getServer().getPlayer(args[1]);

                    if (receiver != null) {
                        User user = this.instance.getUserManager().getUser(receiver);
                        int amount = Integer.parseInt(args[2]);

                        user.getData().setTokens(user.getData().getTokens() + amount);
                        player.sendMessage(Chat.color("&a&lSuccess! &7You have given &a" + amount + " &7tokens to &a" + receiver.getName() + "&7, they now have &a" + user.getData().getTokens() + " tokens&7!"));
                    } else {
                        player.sendMessage(Chat.color("&c&lError! &7I could not find this player, they may not have been loaded."));
                        return false;
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(Chat.color("&c&lError! &7You have entered an invalid amount of tokens to give."));
                }
                break;

            case "revoke":
            case "remove":
                try {
                    Player receiver = this.instance.getServer().getPlayer(args[1]);

                    if (receiver != null) {
                        User user = this.instance.getUserManager().getUser(receiver);
                        int amount = Integer.parseInt(args[2]);

                        user.getData().setTokens(user.getData().getTokens() - amount);
                        player.sendMessage(Chat.color("&a&lSuccess! &7You have revoked &c" + amount + " &7tokens from &c" + receiver.getName() + "&7, they now have &c" + user.getData().getTokens() + " tokens&7!"));
                    } else {
                        player.sendMessage(Chat.color("&c&lError! &7I could not find this player, they may not have been loaded."));
                        return false;
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(Chat.color("&c&lError! &7You have entered an invalid amount of tokens to give."));
                }
                break;

            default:
                player.sendMessage(Chat.color("&c&lError! &7Usage: &o/tokens <action> <details> <amount>\n &8- &7action: give, remove\n &8- &7details: player\n &8- &7amount: integer"));
        }

        return true;
    }
}
