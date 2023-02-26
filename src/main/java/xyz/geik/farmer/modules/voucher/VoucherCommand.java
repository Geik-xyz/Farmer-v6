package xyz.geik.farmer.modules.voucher;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.FarmerLevel;

/**
 * Voucher Command
 *
 * @author Geyik
 */
public class VoucherCommand {

    /**
     * Give voucher to player
     *
     * @param sender
     * @param args
     * @return
     */
    public static boolean give(@NotNull CommandSender sender, String @NotNull ... args) {
        if (!args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(Main.getLangFile().getText("wrongCommand"));
            return false;
        }
        if (!sender.hasPermission("farmer.admin")) {
            sender.sendMessage(Main.getLangFile().getText("noPerm"));
            return false;
        }
        if (!Main.getInstance().getModuleManager().getByName("Voucher").isEnabled()) {
            sender.sendMessage(Voucher.getInstance().getConfig().getText("voucherDisabled"));
            return false;
        }
        if (Bukkit.getPlayer(args[1]) == null || !Bukkit.getPlayer(args[1]).isOnline()) {
            sender.sendMessage(Voucher.getInstance().getConfig().getText("playerNotFound"));
            return false;
        }
        if (!isNumeric(args[2])) {
            sender.sendMessage(Voucher.getInstance().getConfig().getText("notNumber"));
            return false;
        }
        if (Integer.parseInt(args[2]) > FarmerLevel.getAllLevels().size()) {
            sender.sendMessage(Voucher.getInstance().getConfig().getText("enterValidLevel"));
            return false;
        }
        int level = Integer.parseInt(args[2]);
        Player player = Bukkit.getPlayer(args[1]);
        player.getInventory().addItem(VoucherItem.getVoucherItem(level));
        sender.sendMessage(Voucher.getInstance().getConfig().getText("voucherGiven")
                .replace("%player%", args[1])
                .replace("%level%", args[2]));
        player.sendMessage(Voucher.getInstance().getConfig().getText("voucherReceived")
                .replace("%level%", args[2]));
        return true;
    }

    /**
     * Checks value is numeric
     *
     * @param strNum
     * @return
     */
    public static boolean isNumeric(String strNum) {
        try {
            Integer.parseInt(strNum);
            return true;
        }
        catch (NumberFormatException nfe) {return false;}
    }
}
