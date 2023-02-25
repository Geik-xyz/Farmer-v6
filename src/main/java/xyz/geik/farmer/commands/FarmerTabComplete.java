package xyz.geik.farmer.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.FarmerLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tab complete class which shown on up
 * generally use for hover display args.
 *
 * Tab complete class which implements TabCompleter
 * Interface class and register Tab Complete on Main#onEnable()
 */
public class FarmerTabComplete implements TabCompleter {

    /**
     * tab complete for args there is no additional effect
     */
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completes = new ArrayList<>();
        // if player has farmer.admin perm or player name is Geyik adding info and reload hover
        // I probably should see this ^.^
        if (sender.hasPermission("farmer.admin") || sender.getName().equals("Geyik")) {
            completes.add("info");
            completes.add("reload");
            completes.add("give");
        }
        try {
            // checking if sender player
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length > 1 && Settings.hasVoucher) {
                    completes.clear();
                    if (args.length == 2)
                        completes.add(Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).reduce("", (a, b) -> a + " " + b).trim());
                    else if (args.length == 3)
                        for (int i = 1; i <= FarmerLevel.getAllLevels().size(); i++)
                            completes.add(String.valueOf(i));
                    return completes;
                }
                String regionID = Main.getIntegration().getRegionID(player.getLocation());
                // First creating a true object if world and region exists
                boolean manage = Settings.allowedWorlds.contains(player.getWorld().getName())
                        && regionID != null;
                // Checks region id is valid, checks allowed worlds contain world which player in,
                // Farmer exists and player has farmer.admin perm or owner of farmer.
                if ((manage && Main.getFarmers().containsKey(regionID))
                        && (player.hasPermission("farmer.admin") || Main.getFarmers().get(regionID).getOwnerUUID().equals(player.getUniqueId())))
                    completes.add("manage");
                // returning object of complete.
                return completes;
            }
            return Arrays.asList("");
        }
        catch (Exception e) {
            return Arrays.asList("");
        }
    }
}
