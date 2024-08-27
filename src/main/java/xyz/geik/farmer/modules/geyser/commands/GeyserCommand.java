package xyz.geik.farmer.modules.geyser.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.handlers.FarmerItemSellEvent;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerInv;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.farmer.modules.geyser.Geyser;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.module.ModuleManager;
import xyz.geik.glib.shades.triumphteam.cmd.core.BaseCommand;
import xyz.geik.glib.shades.triumphteam.cmd.core.annotation.Command;
import xyz.geik.glib.shades.triumphteam.cmd.core.annotation.Default;
import xyz.geik.glib.shades.triumphteam.cmd.core.annotation.SubCommand;
import xyz.geik.glib.shades.xseries.XMaterial;

import java.util.Arrays;
import java.util.Locale;

/**
 * Geyser Commands
 *
 * @author geyik
 */
@RequiredArgsConstructor
@Command(value = "farmer", alias = {"farm", "çiftçi", "fm", "ciftci"})
public class GeyserCommand extends BaseCommand {

    /**
     * Base command
     * @param sender executor
     */
    @Default
    public void defaultCommand(CommandSender sender) {
        ChatUtils.sendMessage(sender, Main.getLangFile().getMessages().getUnknownCommand());
    }

    /**
     * Sell Basic Command
     * @param sender command sender
     * @param item item
     */
    @SubCommand(value = "sell", alias = {"sat"})
    public void sellCommand(CommandSender sender, String item) {
        if (!ModuleManager.getModule("Geyser").isEnabled()) {
            sender.sendMessage(Geyser.getInstance().getLang().getText("geyserDisabled"));
            return;
        }
        // Checks if sender instanceof player
        if (sender instanceof ConsoleCommandSender)
            return;
        Player player = (Player) sender;
        // Checks item dropped in region of a player
        // And checks region owner has a farmer
        final String regionID;
        try {
            regionID = Main.getIntegration().getRegionID(((Player) sender).getLocation());
            if (regionID == null || !FarmerManager.getFarmers().containsKey(regionID))
                return;
            Farmer farmer = FarmerManager.getFarmers().get(regionID);
            // Sell All command
            if (Geyser.getSellAllCommands().stream().anyMatch(cmd -> cmd.equalsIgnoreCase(item)))
                executeSellEvent(farmer, player, null);
            // Sell only one item command
            else
                executeSellEvent(farmer, player, item);
        }
        catch (NullPointerException ignored) {
        }
    }

    /**
     * Executes sell event for desired command
     * @param farmer region farmer
     * @param player target player
     * @param item to sell item @nullable
     */
    private static void executeSellEvent(Farmer farmer, @NotNull Player player, @Nullable String item) {
        // Checks farmer in collection state
        if (player.hasPermission("farmer.admin") ||
                farmer.getUsers().stream().anyMatch(user -> (
                        !user.getPerm().equals(FarmerPerm.COOP)
                                && user.getName().equalsIgnoreCase(player.getName())))) {
            // If replacer matches then replace the item
            // Sells only one item
            if (item != null) {
                if (Geyser.getNameReplacer().containsKey(item))
                    item = Geyser.getNameReplacer().get(item);
                String checkMaterial = item;
                // If default items does not contain the material
                if (FarmerInv.defaultItems.stream()
                        .noneMatch(defaultItem -> defaultItem.getMaterial().toString().equalsIgnoreCase(checkMaterial))) {
                    player.sendMessage(Geyser.getInstance().getLang().getText("cantFindTheItem"));
                    return;
                }
                FarmerItem toSell = farmer.getInv().getStockedItem(XMaterial.valueOf(item.toUpperCase(Locale.ENGLISH)));
                sellItem(farmer, player, toSell);
            }
            else {
                // Loops all the items
                farmer.getInv().getItems().forEach(farmerItem -> sellItem(farmer, player, farmerItem));
            }
        }
        else
            player.sendMessage(Geyser.getInstance().getLangFile().getText("noPerm"));
    }

    /**
     * Triggers the sell event
     * @param farmer Farmer of region
     * @param player executor
     * @param item to sell item
     */
    private static void sellItem(Farmer farmer, Player player, FarmerItem item) {
        // Sell event execution
        FarmerItemSellEvent itemSellEvent = new FarmerItemSellEvent(farmer, item, player);
        itemSellEvent.setGeyser(true);
        Bukkit.getPluginManager().callEvent(itemSellEvent);
    }
}
