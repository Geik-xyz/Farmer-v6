package xyz.geik.farmer.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.modules.voucher.Voucher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        if (sender.hasPermission("farmer.admin") || sender.getName().equals("Geyik"))
            completes.addAll(Arrays.asList("info", "reload", "give"));
        try {
            if (args.length > 1)
                completes.clear();
            // checking if sender player
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length > 1 && Voucher.getInstance().isEnabled() && args[0].equalsIgnoreCase("give")) {
                    completes.clear();
                    if (args.length == 2)
                        completes.addAll(Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
                    else if (args.length == 3)
                        for (int i = 1; i <= FarmerLevel.getAllLevels().size(); i++)
                            completes.add(String.valueOf(i));
                    return completes;
                }
                String regionID = Main.getIntegration().getRegionID(player.getLocation());
                // First creating a true object if world and region exists
                boolean manage = Settings.isWorldAllowed(player.getWorld().getName())
                        && regionID != null;
                // Checks region id is valid, checks allowed worlds contain world which player in,
                // Farmer exists and player has farmer.admin perm or owner of farmer.
                if ((manage && FarmerManager.getFarmers().containsKey(regionID))
                        && (player.hasPermission("farmer.admin") || FarmerManager.getFarmers().get(regionID).getOwnerUUID().equals(player.getUniqueId())))
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
