package xyz.geik.farmer.commands;

import com.cryptomorin.xseries.messages.Titles;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.guis.BuyGui;
import xyz.geik.farmer.guis.MainGui;
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
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                farmerBaseCommand(player);
            } else if (args.length == 1) {
                oneArgCommands(player, args[0]);
            } else if (args.length == 2) {
                twoArgCommands(player, args);
            } else if (args.length == 3) {
                VoucherCommand.give(sender, args);
            }
        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload"))
                    reloadCommand(sender);
                else if (args[0].equals("fix")) {
                    int player_count = Bukkit.getOnlinePlayers().size();
                    if (player_count == 0)
                        Main.getInstance().getSql().fixDatabase();
                    else
                        Main.getInstance().getLogger().info("You can run this command only when no player online!");
                } else if (args[0].equalsIgnoreCase("about")) {
                    aboutCommand(sender);
                }
            }
            else if (args.length == 3)
                VoucherCommand.give(sender, args);
        }
        return false;
    }


    /**
     * Base command for coop, member, owner
     * It basically open farmer buy gui unless don't have it.
     * Open farmer inventory gui if has farmer.
     *
     * @param player
     */
    private void farmerBaseCommand(@NotNull Player player) {
        if (!Settings.isWorldAllowed(player.getWorld().getName())) {
            player.sendMessage(Main.getLangFile().getText("wrongWorld"));
            return;
        }
        String regionID = getRegionID(player);
        if (regionID == null)
            player.sendMessage(Main.getLangFile().getText("noRegion"));
        else if (!FarmerManager.getFarmers().containsKey(regionID)) {
            // Using this uuid for owner check
            UUID owner = Main.getIntegration().getOwnerUUID(regionID);
            // Owner check for buy
            if (owner.equals(player.getUniqueId())) {
                if (Settings.buyFarmer)
                    BuyGui.showGui(player);
                else {
                    Titles.sendTitle(player, Main.getLangFile().getText("buyDisabled.title"),
                            Main.getLangFile().getText("buyDisabled.subtitle"));
                }
            }
            else
                player.sendMessage(Main.getLangFile().getText("mustBeOwner"));
        } else {
            // Perm && user check
            if (FarmerManager.getFarmers().get(regionID).getUsers().stream()
                            .anyMatch(usr -> (usr.getUuid().equals(player.getUniqueId()))))
                MainGui.showGui(player, FarmerManager.getFarmers().get(regionID));
            else
                player.sendMessage(Main.getLangFile().getText("noPerm"));
        }
    }

    /**
     * Removes farmer from command sender.
     *
     * @param player
     */
    private void selfRemoveCommand(@NotNull Player player) {
        String regionID = getRegionID(player);
        if (regionID == null)
            player.sendMessage(Main.getLangFile().getText("noRegion"));

        UUID ownerUUID = Main.getIntegration().getOwnerUUID(regionID);
        // Custom perm check for remove command
        if (player.hasPermission("farmer.remove") && ownerUUID.equals(player.getUniqueId()) || player.hasPermission("farmer.admin")) {
            // Removing by #FarmerAPI and sending message by result
            boolean result = FarmerAPI.getFarmerManager().removeFarmer(regionID);
            if (result)
                player.sendMessage(Main.getLangFile().getText("removedFarmer"));
        } else {
            player.sendMessage(Main.getLangFile().getText("noPerm"));
        }
    }

    /**
     * Sends the player information about the Farmer plugin
     *
     * @param player
     */
    private void aboutCommand(@NotNull CommandSender player) {
        player.sendMessage(Main.color("&7&m----------------------------------------"));
        player.sendMessage(Main.color("#FFA500          FARMER &7- &6v" + Main.getInstance().getDescription().getVersion()));
        player.sendMessage(Main.color("#3CB371Author: #90EE90Geik"));
        player.sendMessage(Main.color("#FF7F50Contributors: #FFA07AAmowny&f, #FFA07AWaterArchery&f, #FFA07AHeron4gf&7, #FFA07Amehmet-27"));
        player.sendMessage(Main.color("#7289DADiscord: &7&ohttps://discord.geik.xyz"));
        player.sendMessage(Main.color("#FFD700Website: &7&ohttps://geik.xyz"));
        player.sendMessage(Main.color("&7&m----------------------------------------"));
        player.sendMessage(Main.color("&aAPI: &7" + Main.getIntegration().getClass().getName()));
        player.sendMessage(Main.color("&aActive Farmer: &7" + FarmerManager.getFarmers().size() ));
        player.sendMessage(Main.color("&7&m----------------------------------------"));
    }

    /**
     * Prints info about farmer which located on player location.
     * It can be usable who has farmer.admin permission, Geyik username and owner
     * I have permission to use it for debugs and support.
     *
     * @param player
     */
    private void infoCommand(@NotNull Player player) {
        String regionID = getRegionID(player);
        if (regionID == null)
            player.sendMessage(Main.getLangFile().getText("noRegion"));
        else if (!FarmerManager.getFarmers().containsKey(regionID))
            player.sendMessage(Main.getLangFile().getText("noFarmer"));
        else {
            Farmer farmer = FarmerManager.getFarmers().get(regionID);
            player.sendMessage(Main.color("&c----------------------"));
            player.sendMessage(Main.color("&bRegion ID: &f" + regionID));
            player.sendMessage(Main.color("&bID: &f" + farmer.getId()));
            player.sendMessage(Main.color("&bOwner: &f" + Bukkit.getOfflinePlayer(farmer.getOwnerUUID()).getName()));
            player.sendMessage(Main.color("&bLevel: &f" + FarmerLevel.getAllLevels().indexOf(farmer.getLevel())));
            player.sendMessage(Main.color("&c----------------------"));
            farmer.getUsers().stream().forEach(key -> {
                player.sendMessage(Main.color("&b" +
                        Bukkit.getOfflinePlayer(key.getUuid()).getName() + " &f- &3" + key.getPerm().name()));
            });
            player.sendMessage(Main.color("&c----------------------"));
            farmer.getInv().getItems().stream().forEach(key -> {
                player.sendMessage(Main.color("&6" + key.getMaterial().name() + " &e" + key.getAmount()));
            });
            player.sendMessage(Main.color("&c----------------------"));
            farmer.getModuleAttributes().forEach((key, value) -> {
                player.sendMessage(Main.color("&a" + key + " &f- &3" + value));
            });
        }
    }

    /**
     * Reload command method which reloads everything it can
     *
     * @param sender
     */
    private void reloadCommand(@NotNull CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            long time = System.currentTimeMillis();
            // Saves all farmer
            Main.getInstance().getSql().updateAllFarmers();
            // Clears cached farmers
            FarmerManager.getFarmers().clear();
            // Regenerates settings
            Settings.regenSettings();
            // Reloading items it also clears old list
            new ItemsLoader();
            // Reloading levels it also clears old list
            FarmerLevel.loadLevels();
            // Reloading farmers again.
            Main.getInstance().getSql().loadAllFarmers();
            FarmerAPI.getModuleManager().getModuleList().forEach(FarmerModule::onReload);
            // Sends message to sender who send this command and also calculating millisecond difference.
            sender.sendMessage(Main.getLangFile().getText("reloadSuccess").replace("%ms%",
                    System.currentTimeMillis() - time + "ms"));
        });
    }

    /**
     * One arg commands which about, info, reload and remove commands
     * Manage usable by administrator or owner of farmer
     * Remove, reload, about and info are administrator commands
     *
     * @param player
     * @param arg
     */
    public void oneArgCommands(@NotNull Player player, String arg) {
        // Checking perm if sender is player and if they don't have perm just returns task
        if ((!player.hasPermission("farmer.admin") && !player.getName().equalsIgnoreCase("Geyik")) && !arg.equalsIgnoreCase("remove")) {
            player.sendMessage(Main.getLangFile().getText("noPerm"));
            return;
        }
        // Check world is suitable for farmer
        if (!Settings.isWorldAllowed(player.getWorld().getName())
                && !arg.equalsIgnoreCase("reload")) {
            player.sendMessage(Main.getLangFile().getText("wrongWorld"));
            return;
        }
        // About command caller
        if (arg.equalsIgnoreCase("about")) {
            aboutCommand(player);
        // Info command caller
        } else if (arg.equalsIgnoreCase("info")) {
            infoCommand(player);
        // Reload command caller
        } else if (arg.equalsIgnoreCase("reload")) {
            reloadCommand(player);
            // Remove command caller
        } else if (arg.equalsIgnoreCase("remove")) {
            selfRemoveCommand(player);
        }
    }

    /**
     * Two arg commands which open and remove commands
     * Manage usable by administrator
     * Remove and open are administrator commands
     *
     * @param player
     * @param arg
     */
    public void twoArgCommands(@NotNull Player player, String @NotNull ... arg) {
        if ((!player.hasPermission("farmer.admin"))) {
            player.sendMessage(Main.getLangFile().getText("noPerm"));
            return;
        }
        // Check world is suitable for farmer
        if (!Settings.isWorldAllowed(player.getWorld().getName())) {
            player.sendMessage(Main.getLangFile().getText("wrongWorld"));
            return;
        }
        // Open command caller
        if (arg[0].equalsIgnoreCase("open")) {
            Player target = Bukkit.getOfflinePlayer(arg[1]).getPlayer();

            String regionID = getRegionID(target);
            if (regionID == null)
                player.sendMessage(Main.getLangFile().getText("noRegion"));

            if (!FarmerManager.getFarmers().containsKey(regionID))
                player.sendMessage(Main.getLangFile().getText("noFarmer"));
            else {
                if (FarmerManager.getFarmers().get(regionID).getUsers().stream().anyMatch(usr -> (usr.getUuid().equals(target.getUniqueId()))))
                    MainGui.showGui(player, FarmerManager.getFarmers().get(regionID));
            }

        }
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

}