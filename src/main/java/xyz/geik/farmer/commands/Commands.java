package xyz.geik.farmer.commands;

import com.bgsoftware.superiorskyblock.api.handlers.ModulesManager;
import com.cryptomorin.xseries.messages.Titles;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.managers.ModuleManager;
import xyz.geik.farmer.database.DBQueries;
import xyz.geik.farmer.guis.BuyGui;
import xyz.geik.farmer.guis.MainGui;
import xyz.geik.farmer.guis.ModuleGui;
import xyz.geik.farmer.helpers.ItemsLoader;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.farmer.modules.voucher.VoucherCommand;

import java.util.UUID;

/**
 * Main command class which implements CommandExecutor
 * Interface class and register command on Main#onEnable()
 */
public class Commands implements CommandExecutor {

    /**
     * Main section of commands executing
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Checking if sender instanceof player
        if (sender instanceof Player){
            Player player = (Player) sender;
            // No arg main command
            if (args.length == 0)
                farmerBaseCommand(player);
            // 1 arg for 1 arg commands
            else if (args.length == 1)
                oneArgCommands(player, args[0]);
            else if (args.length == 3)
                VoucherCommand.give(sender, args);
        }
        // Also console section here for reload command.
        else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload"))
                    reloadCommand(sender);
            }
            else if (args.length == 3)
                    VoucherCommand.give(sender, args);
        }
        return false;
    }

    /**
     * One arg commands which reload, manage, info and remove commands
     * Manage usable by administrator or owner of farmer
     * Remove, reload and info are administrator commands
     *
     * @param player
     * @param arg
     * @return
     */
    public boolean oneArgCommands(@NotNull Player player, String arg) {
        // Checking perm if sender is player and if they don't have perm just returns task
        if ((!player.hasPermission("farmer.admin") && !player.getName().equalsIgnoreCase("Geyik"))
                && !arg.equalsIgnoreCase("manage")) {
            player.sendMessage(Main.getLangFile().getText("noPerm"));
            return false;
        }
        // Check world is suitable for farmer
        if (!Settings.isWorldAllowed(player.getWorld().getName())
                && !arg.equalsIgnoreCase("reload")) {
            player.sendMessage(Main.getLangFile().getText("wrongWorld"));
            return false;
        }
        // Reload command caller
        if (arg.equalsIgnoreCase("reload"))
            reloadCommand(player);
        // Manage command caller
        else if (arg.equalsIgnoreCase("manage"))
            farmerBaseCommand(player);
        // Info command caller
        else if (arg.equalsIgnoreCase("info"))
            infoCommand(player);
        // Remove command caller
        else if (arg.equalsIgnoreCase("remove"))
            removeFarmerCommand(player);
        return true;
    }

    /**
     * Prints info about farmer which located on player location.
     * It can be usable who has farmer.admin permission, Geyik username and owner
     * I have permission to use it for debugs and support.
     *
     * @param player
     * @return
     */
    private boolean infoCommand(@NotNull Player player) {
        // My debug command for bug reports
        if (player.getName().equalsIgnoreCase("Geyik")) {
            player.sendMessage(Main.color("&aVersion: &7" + Main.getInstance().getDescription().getVersion()));
            player.sendMessage(Main.color("&aAPI: &7" + Main.getIntegration().getClass().getName()));
            player.sendMessage(Main.color("&aActive Farmer: &7" + FarmerAPI.getFarmerManager().getFarmers().size() ));
        }
        // Catching region id and checking is it null or don't have farmer
        String regionID = Main.getIntegration().getRegionID(player.getLocation());
        if (regionID == null)
            player.sendMessage(Main.getLangFile().getText("noRegion"));
        else if (!FarmerAPI.getFarmerManager().getFarmers().containsKey(regionID))
            player.sendMessage(Main.getLangFile().getText("noFarmer"));
        else {
            // After all the checks loading farmer
            Farmer farmer = FarmerAPI.getFarmerManager().getFarmers().get(regionID);
            player.sendMessage(Main.color("&c----------------------"));
            farmer.getUsers().stream().forEach(key -> {
                player.sendMessage(Main.color("&b" +
                        Bukkit.getOfflinePlayer(key.getUuid()).getName() + " &f- &3" + key.getPerm().name()));
            });
            player.sendMessage(Main.color("&c----------------------"));
            farmer.getInv().getItems().stream().forEach(key -> {
                player.sendMessage(Main.color("&6" + key.getMaterial().name() + " &e" + key.getAmount()));
            });
        }
        return true;
    }

    /**
     * Reload command method which reloads everything it can
     *
     * @param sender
     * @return
     */
    private boolean reloadCommand(@NotNull CommandSender sender) {
        // Creating time long for calculating time it takes.
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            long time = System.currentTimeMillis();
            // Saves all farmer
            DBQueries.updateAllFarmers();
            // Clears cached farmers
            FarmerAPI.getFarmerManager().getFarmers().clear();
            // Regenerates settings
            Settings.regenSettings();
            // Reloading items it also clears old list
            new ItemsLoader();
            // Reloading levels it also clears old list
            FarmerLevel.loadLevels();
            // Reloading farmers again.
            DBQueries.loadAllFarmers();
            FarmerAPI.getModuleManager().getModuleList().forEach(FarmerModule::onReload);
            // Sends message to sender who send this command and also calculating millisecond difference.
            sender.sendMessage(Main.getLangFile().getText("reloadSuccess").replace("%ms%",
                    System.currentTimeMillis() - time + "ms"));
        });
        return true;
    }


    /**
     * Removes farmer where command sender at.
     *
     * @param player
     * @return
     */
    private boolean removeFarmerCommand(Player player) {
        // Checks if region id suitable for farmer
        String regionID = getRegionID(player);
        if (regionID == null)
            player.sendMessage(Main.getLangFile().getText("noRegion"));

        // Removing by #FarmerAPI and sending message by result
        boolean result = FarmerAPI.getFarmerManager().removeFarmer(regionID);
        if (result)
            player.sendMessage(Main.getLangFile().getText("removedFarmer"));
        return result;
    }

    /**
     * Gets region id with #Integration
     * if there has a region.
     *
     * @param player
     * @return
     */
    private String getRegionID(Player player) {
        String regionID;
        // Simple try catch method for
        // compatibility with all plugins
        try {
            regionID = Main.getIntegration().getRegionID(player.getLocation());
        }
        catch (Exception e) {
            regionID = null;
        }
        return regionID;
    }

    /**
     * Base command for coop, member, owner
     * and ofc for administrator. It basically
     * open farmer buy gui unless don't have it.
     * Open farmer inventory gui if has farmer.
     *
     * @param player
     * @return
     */
    private boolean farmerBaseCommand(Player player) {
        // There is another world check
        if (!Settings.isWorldAllowed(player.getWorld().getName())) {
            player.sendMessage(Main.getLangFile().getText("wrongWorld"));
            return true;
        }
        // and also one more region check
        String regionID = getRegionID(player);
        if (regionID == null)
            player.sendMessage(Main.getLangFile().getText("noRegion"));
        else if (!FarmerAPI.getFarmerManager().getFarmers().containsKey(regionID)) {
            // Using this uuid for owner check
            UUID owner = Main.getIntegration().getOwnerUUID(regionID);
            // Owner check for buy
            if (owner.equals(player.getUniqueId()) || player.hasPermission("farmer.admin")) {
                if (Settings.buyFarmer)
                    BuyGui.showGui(player);
                else {
                    Titles.sendTitle(player, Main.getLangFile().getText("buyDisabled.title"),
                            Main.getLangFile().getText("buyDisabled.subtitle"));
                }
            }
            else
                player.sendMessage(Main.getLangFile().getText("mustBeOwner"));
        }
        else {
            // Perm && user check
            if (player.hasPermission("farmer.admin") ||
                    FarmerAPI.getFarmerManager().getFarmers().get(regionID).getUsers().stream()
                            .anyMatch(usr -> (usr.getUuid().equals(player.getUniqueId()))))
                MainGui.showGui(player, FarmerAPI.getFarmerManager().getFarmers().get(regionID));
            else
                player.sendMessage(Main.getLangFile().getText("noPerm"));
        }
        return true;
    }
}