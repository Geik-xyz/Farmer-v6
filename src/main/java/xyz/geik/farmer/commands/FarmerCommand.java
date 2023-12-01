package xyz.geik.farmer.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.guis.BuyGui;
import xyz.geik.farmer.guis.MainGui;
import xyz.geik.farmer.helpers.CacheLoader;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.chat.Placeholder;
import xyz.geik.glib.shades.triumphteam.cmd.bukkit.annotation.Permission;
import xyz.geik.glib.shades.triumphteam.cmd.core.BaseCommand;
import xyz.geik.glib.shades.triumphteam.cmd.core.annotation.Command;
import xyz.geik.glib.shades.triumphteam.cmd.core.annotation.Default;
import xyz.geik.glib.shades.triumphteam.cmd.core.annotation.SubCommand;
import xyz.geik.glib.shades.xseries.messages.Titles;

import java.util.Arrays;
import java.util.UUID;

/**
 * Farmer command class
 * for farmer commands
 *
 * @author geik, amownyy
 * @since v6-b100
 */
@RequiredArgsConstructor
@Command(value = "farmer", alias = {"farm", "çiftçi", "fm", "ciftci"})
public class FarmerCommand extends BaseCommand {

    /**
     * Default command of farmer
     *
     * @param player the command executor
     */
    @Default
    public void defaultCommand(@NotNull Player player) {
        if (!Main.getConfigFile().getSettings().getAllowedWorlds().contains(player.getWorld().getName())) {
            ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getWrongWorld());
            return;
        }
        String regionID = getRegionID(player);
        if (regionID == null)
            ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNoRegion());
        else if (!FarmerManager.getFarmers().containsKey(regionID)) {
            // Using this uuid for owner check
            UUID owner = Main.getIntegration().getOwnerUUID(regionID);
            // Owner check for buy
            if (owner.equals(player.getUniqueId())) {
                if (Main.getConfigFile().getSettings().isBuyFarmer())
                    BuyGui.showGui(player);
                else {
                    Titles.sendTitle(player, Main.getLangFile().getBuyDisabled().getTitle(),
                            Main.getLangFile().getBuyDisabled().getSubtitle());
                }
            }
            else
                ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getMustBeOwner());
        } else {
            // Perm && user check
            if (FarmerManager.getFarmers().get(regionID).getUsers().stream()
                    .anyMatch(usr -> (usr.getUuid().equals(player.getUniqueId()))))
                MainGui.showGui(player, FarmerManager.getFarmers().get(regionID));
            else
                ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNoPerm());
        }
    }

    /**
     * Remove command of farmer
     *
     * @param player
     */
    @SubCommand("remove")
    public void removeCommand(@NotNull Player player) {
        String regionID = getRegionID(player);
        if (regionID == null)
            ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNoRegion());

        UUID ownerUUID = Main.getIntegration().getOwnerUUID(regionID);
        // Custom perm check for remove command
        if (player.hasPermission("farmer.remove") && ownerUUID.equals(player.getUniqueId()) || player.hasPermission("farmer.admin")) {
            // Removing by #FarmerAPI and sending message by result
            boolean result = FarmerAPI.getFarmerManager().removeFarmer(regionID);
            if (result)
                ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getRemovedFarmer());
        } else
            ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNoPerm());
    }

    /**
     * About command of farmer
     *
     * @param player
     */
    @Permission("farmer.admin")
    @SubCommand("about")
    public void aboutCommand(@NotNull CommandSender player) {
        player.sendMessage(ChatUtils.color("&7&m----------------------------------------"));
        player.sendMessage(ChatUtils.color("#FFA500          FARMER &7- &6" + Main.getInstance().getDescription().getVersion()));
        player.sendMessage(ChatUtils.color("#3CB371Author: #90EE90Geik"));
        player.sendMessage(ChatUtils.color("#FF7F50Contributors: #FFA07A" + Arrays.toString(Main.getInstance().getDescription().getAuthors().toArray())));
        player.sendMessage(ChatUtils.color("#7289DADiscord: &7&ohttps://discord.geik.xyz"));
        player.sendMessage(ChatUtils.color("#FFD700Website: &7&ohttps://geik.xyz"));
        player.sendMessage(ChatUtils.color("&7&m----------------------------------------"));
        player.sendMessage(ChatUtils.color("&aAPI: &7" + Main.getIntegration().getClass().getName()));
        player.sendMessage(ChatUtils.color("&aEconomy API: &7" + Main.getEconomy().getClass().getName()));
        player.sendMessage(ChatUtils.color("&aActive Farmer: &7" + FarmerManager.getFarmers().size()));
        player.sendMessage(ChatUtils.color("&7&m----------------------------------------"));
    }

    /**
     * Info command of farmer
     *
     * @param player
     */
    @Permission("farmer.admin")
    @SubCommand("info")
    public void infoCommand(@NotNull Player player) {
        String regionID = getRegionID(player);
        if (regionID == null)
            ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNoRegion());
        else if (!FarmerManager.getFarmers().containsKey(regionID))
            ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNoFarmer());
        else {
            Farmer farmer = FarmerManager.getFarmers().get(regionID);
            player.sendMessage(ChatUtils.color("&c----------------------"));
            player.sendMessage(ChatUtils.color("&bRegion ID: &f" + regionID));
            player.sendMessage(ChatUtils.color("&bID: &f" + farmer.getId()));
            player.sendMessage(ChatUtils.color("&bOwner: &f" + Bukkit.getOfflinePlayer(farmer.getOwnerUUID()).getName()));
            player.sendMessage(ChatUtils.color("&bLevel: &f" + FarmerLevel.getAllLevels().indexOf(farmer.getLevel())));
            player.sendMessage(ChatUtils.color("&c----------------------"));
            farmer.getUsers().stream().forEach(key -> {
                player.sendMessage(ChatUtils.color("&b" +
                        Bukkit.getOfflinePlayer(key.getUuid()).getName() + " &f- &3" + key.getPerm().name()));
            });
            player.sendMessage(ChatUtils.color("&c----------------------"));
            farmer.getInv().getItems().stream().forEach(key -> {
                player.sendMessage(ChatUtils.color("&6" + key.getMaterial().name() + " &e" + key.getAmount()));
            });
            player.sendMessage(ChatUtils.color("&c----------------------"));
            farmer.getModuleAttributes().forEach((key, value) -> {
                player.sendMessage(ChatUtils.color("&a" + key + " &f- &3" + value));
            });
        }
    }

    /**
     * Reload command of farmer
     *
     * @param sender
     */
    @Permission("farmer.admin")
    @SubCommand("reload")
    public void reloadCommand(@NotNull CommandSender sender) {
        if (!sender.hasPermission("farmer.admin")) {
            ChatUtils.sendMessage(sender, Main.getLangFile().getMessages().getNoPerm());
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            long time = System.currentTimeMillis();
            // Saves all farmer
            Main.getInstance().getSql().updateAllFarmers();
            // Clears cached farmers
            FarmerManager.getFarmers().clear();
            // Regenerates settings
            Main.getInstance().getConfigFile().load(true);
            Main.getInstance().getLangFile().load(true);
            // Reloading items it also clears old list
            // Reloading levels it also clears old list
            CacheLoader.loadAllItems();
            CacheLoader.loadAllLevels();
            // Reloading farmers again.
            Main.getInstance().getSql().loadAllFarmers();
            FarmerAPI.getModuleManager().getModuleList().forEach(FarmerModule::onReload);
            // Sends message to sender who send this command and also calculating millisecond difference.
            ChatUtils.sendMessage(sender, Main.getLangFile().getMessages().getReloadSuccess(),
                    new Placeholder("%ms%", System.currentTimeMillis() - time + "ms"));
        });
    }

    /**
     * Open command of farmer
     *
     * @param player
     * @param arg
     */
    @Permission("farmer.admin")
    @SubCommand("open")
    public void openCommand(@NotNull Player player, String @NotNull ... arg) {
        if ((!player.hasPermission("farmer.admin"))) {
            ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNoPerm());
            return;
        }
        // Check world is suitable for farmer
        if (!Main.getConfigFile().getSettings().getAllowedWorlds().contains(player.getWorld().getName())) {
            ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getWrongWorld());
            return;
        }
        // Open command caller
        if (arg[0].equalsIgnoreCase("open")) {
            Player target = Bukkit.getOfflinePlayer(arg[1]).getPlayer();

            String regionID = getRegionID(target);
            if (regionID == null)
                ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNoRegion());

            if (!FarmerManager.getFarmers().containsKey(regionID))
                ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNoFarmer());
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
     * @param player the command executor
     * @return String of region
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