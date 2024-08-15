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
import xyz.geik.farmer.helpers.WorldHelper;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.chat.Placeholder;
import xyz.geik.glib.module.ModuleManager;
import xyz.geik.glib.shades.triumphteam.cmd.bukkit.annotation.Permission;
import xyz.geik.glib.shades.triumphteam.cmd.core.BaseCommand;
import xyz.geik.glib.shades.triumphteam.cmd.core.annotation.Command;
import xyz.geik.glib.shades.triumphteam.cmd.core.annotation.Default;
import xyz.geik.glib.shades.triumphteam.cmd.core.annotation.SubCommand;
import xyz.geik.glib.shades.xseries.messages.Titles;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

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
     * @param sender the command executor
     */
    @Default
    public void defaultCommand(@NotNull CommandSender sender) {
        if (!(sender instanceof Player)) {
            ChatUtils.sendMessage(sender, Main.getLangFile().getMessages().getUnknownCommand());
            return;
        }
        Player player = (Player) sender;
        if (!WorldHelper.isFarmerAllowed(player.getWorld().getName())) {
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
            if (player.hasPermission("farmer.admin") ||
                    FarmerManager.getFarmers().get(regionID).getUsers().stream()
                            .anyMatch(usr -> (usr.getUuid().equals(player.getUniqueId()))))
                MainGui.showGui(player, FarmerManager.getFarmers().get(regionID));
            else
                ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNoPerm());
        }
    }

    /**
     * Remove command of farmer
     *
     * @param sender command sender
     */
    @Permission("farmer.admin")
    @SubCommand(value = "remove", alias = {"sil", "r"})
    public void removeCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            ChatUtils.sendMessage(sender, Main.getLangFile().getMessages().getUnknownCommand());
            return;
        }
        Player player = (Player) sender;
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
     * @param player command sender
     */
    @SubCommand(value = "about", alias = {"hakkında", "pl", "ver", "version", "bilgi"})
    public void aboutCommand(@NotNull CommandSender player) {
        if (!(player.hasPermission("farmer.admin")) &&
                ((player instanceof Player)
                        || (player.getName().equals("Geyik")
                        || player.getName().equals("Amownyy")))) {
            ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNoPerm());
            return;
        }
        player.sendMessage(ChatUtils.color("&7&m----------------------------------------"));
        player.sendMessage(ChatUtils.color("&3          FARMER &7- &6" + Main.getInstance().getDescription().getVersion()));
        player.sendMessage(ChatUtils.color("&3Author: &4Geik"));
        player.sendMessage(ChatUtils.color("&3Contributors: &c" + Arrays.toString(Main.getInstance().getDescription().getAuthors().toArray())));
        player.sendMessage(ChatUtils.color("&3Discord: &b&ohttps://discord.gg/yP7jQdvc6d"));
        player.sendMessage(ChatUtils.color("&3Website: &d&ohttps://geik.xyz"));
        player.sendMessage(ChatUtils.color("&7&m----------------------------------------"));
        player.sendMessage(ChatUtils.color("&aAPI: &7" + Main.getIntegration().getClass().getSimpleName()));
        player.sendMessage(ChatUtils.color("&aEconomy API: &7" + Main.getEconomy().getClass().getSimpleName()));
        player.sendMessage(ChatUtils.color("&aActive Farmer: &7" + FarmerManager.getFarmers().size()));
        ChatUtils.sendMessage(player, "&aLanguage: &7" + Main.getConfigFile().getSettings().getLang());
        ChatUtils.sendMessage(player, "&aModules: &7" + Arrays.toString(ModuleManager.getModules().values().stream().map(module -> module.getName()).collect(Collectors.toList()).toArray()));
        player.sendMessage(ChatUtils.color("&7&m----------------------------------------"));
    }

    /**
     * Info command of farmer
     *
     * @param sender command sender
     */
    @Permission("farmer.admin")
    @SubCommand(value = "info", alias = {"bilgi", "inf"})
    public void infoCommand(@NotNull CommandSender sender) {
        if (!(sender instanceof Player)) {
            ChatUtils.sendMessage(sender, Main.getLangFile().getMessages().getUnknownCommand());
            return;
        }
        Player player = (Player) sender;
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
            farmer.getUsers().stream().forEach(key -> player.sendMessage(ChatUtils.color("&b" +
                    Bukkit.getOfflinePlayer(key.getUuid()).getName() + " &f- &3" + key.getPerm().name())));
            player.sendMessage(ChatUtils.color("&c----------------------"));
            farmer.getInv().getItems().stream().forEach(key -> player.sendMessage(ChatUtils.color("&6" + key.getMaterial().name() + " &e" + key.getAmount())));
            player.sendMessage(ChatUtils.color("&c----------------------"));
            farmer.getModuleAttributes().forEach((key, value) -> player.sendMessage(ChatUtils.color("&a" + key + " &f- &3" + value)));
        }
    }

    /**
     * Reload command of farmer
     *
     * @param sender command sender
     */
    @Permission("farmer.admin")
    @SubCommand(value = "reload", alias = {"rl", "yenile"})
    public void reloadCommand(@NotNull CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            long time = System.currentTimeMillis();
            // Saves all farmer
            Main.getSql().updateAllFarmers();
            // Clears cached farmers
            FarmerManager.getFarmers().clear();
            // Regenerates settings
            Main.getConfigFile().load(true);
            Main.getLangFile().load(true);
            Main.getModulesFile().load(true);
            // Reloading items it also clears old list
            // Reloading levels it also clears old list
            CacheLoader.loadAllItems();
            CacheLoader.loadAllLevels();
            // Reloading farmers again.
            Main.getSql().loadAllFarmers();
            Main.getInstance().getModuleManager().reloadModules();
            FarmerModule.calculateModulesUseGui();
            WorldHelper.loadAllowedWorlds();
            // Sends message to sender who send this command and also calculating millisecond difference.
            ChatUtils.sendMessage(sender, Main.getLangFile().getMessages().getReloadSuccess(),
                    new Placeholder("%ms%", System.currentTimeMillis() - time + "ms"));
        });
    }

    /**
     * Open command of farmer
     *
     * @param sender command sender
     * @param target target player
     */
    @Permission("farmer.admin")
    @SubCommand(value = "open", alias = {"aç"})
    public void openCommand(@NotNull CommandSender sender, String target) {
        Player player = Bukkit.getPlayerExact(target);
        if (player == null) {
            ChatUtils.sendMessage(sender, Main.getLangFile().getMessages().getTargetPlayerNotAvailable());
            return;
        }
        if (!player.isOnline()) {
            ChatUtils.sendMessage(sender, Main.getLangFile().getMessages().getPlayerNotOnline());
            return;
        }
        // Check world is suitable for farmer
        if (!WorldHelper.isFarmerAllowed(player.getWorld().getName())) {
            ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getWrongWorld());
            return;
        }

        String regionID = getRegionID(player);
        if (regionID == null)
            ChatUtils.sendMessage(sender, Main.getLangFile().getMessages().getNoRegion());

        if (!FarmerManager.getFarmers().containsKey(regionID))
            ChatUtils.sendMessage(sender, Main.getLangFile().getMessages().getNoFarmer());
        else {
            MainGui.showGui(player, FarmerManager.getFarmers().get(regionID));
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