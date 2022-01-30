package net.lotho.commands;

import net.lotho.Azazel;
import net.lotho.interfaces.TokensMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagsCMD implements CommandExecutor {

    private final Azazel instance = Azazel.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        new TokensMenu(this.instance).open((Player) sender);
        return true;
    }
}
