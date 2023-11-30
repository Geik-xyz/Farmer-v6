package xyz.geik.farmer.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.modules.voucher.Voucher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tab complete class which shown on up
 * generally use for hover display args.
 * <p>
 * Tab complete class which implements TabCompleter
 * Interface class and register Tab Complete on Main#onEnable()
 */
public class FarmerTabComplete implements TabCompleter {

    /**
     * Constructor of class
     */
    public FarmerTabComplete() {}

    /**
     * tab complete for args there is no additional effect
     */
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender.hasPermission("farmer.admin") || sender.getName().equals("Geyik")) {
            List<String> onlinePlayers = getOnlinePlayers();
            if (args.length == 1) {
                return Arrays.asList("about", "info", "reload", "give", "open", "remove", "fix");
            }
            if (args.length > 1 && Voucher.getInstance().isEnabled() && args[0].equalsIgnoreCase("give")) {
                if (args.length == 2) {
                    return getOnlinePlayers();
                }
                else if (args.length == 3) {
                    for (int i = 1; i <= FarmerLevel.getAllLevels().size(); i++) {
                        return Collections.singletonList(String.valueOf(i));
                    }
                }
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("open")) {
                return getOnlinePlayers();
            } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
                return getOnlinePlayers();
            }
        } else {
            if (args.length == 1) {
                return Collections.singletonList("remove");
            }
        }
        return null;
    }

    /**
     * Gets online players
     *
     * @return List of online players
     */
    private static @NotNull List<String> getOnlinePlayers(){
        List<String> online = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach((player -> online.add(player.getDisplayName())));
        return online;
    }

}
