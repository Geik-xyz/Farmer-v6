package xyz.geik.farmer.modules.voucher.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.modules.voucher.Voucher;
import xyz.geik.farmer.modules.voucher.helper.VoucherItem;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.module.ModuleManager;
import xyz.geik.glib.shades.triumphteam.cmd.core.BaseCommand;
import xyz.geik.glib.shades.triumphteam.cmd.core.annotation.Command;
import xyz.geik.glib.shades.triumphteam.cmd.core.annotation.Default;
import xyz.geik.glib.shades.triumphteam.cmd.core.annotation.SubCommand;

/**
 * Voucher Command
 *
 * @author geyik, amownyy
 */
@RequiredArgsConstructor
@Command(value = "farmer", alias = {"farm", "çiftçi", "fm", "ciftci"})
public class VoucherCommand extends BaseCommand {

    /**
     * Base command
     * @param sender executor
     */
    @Default
    public void defaultCommand(CommandSender sender) {
        ChatUtils.sendMessage(sender, Main.getLangFile().getMessages().getUnknownCommand());
    }

    @SubCommand(value = "give", alias = {"ver"})
    public void giveCommand(CommandSender sender, String target, String amount) {
        if (!sender.hasPermission("farmer.admin")) {
            ChatUtils.sendMessage(sender, Main.getLangFile().getMessages().getNoPerm());
            return;
        }
        if (!ModuleManager.getModule("Voucher").isEnabled()) {
            sender.sendMessage(Voucher.getInstance().getLang().getText("voucherDisabled"));
            return;
        }
        if (Bukkit.getPlayer(target) == null || !Bukkit.getPlayer(target).isOnline()) {
            sender.sendMessage(Voucher.getInstance().getLang().getText("playerNotFound"));
            return;
        }
        if (!isNumeric(amount)) {
            sender.sendMessage(Voucher.getInstance().getLang().getText("notNumber"));
            return;
        }
        if (Integer.parseInt(amount) > FarmerLevel.getAllLevels().size()) {
            sender.sendMessage(Voucher.getInstance().getLang().getText("enterValidLevel"));
            return;
        }
        int level = Integer.parseInt(amount);
        Player player = Bukkit.getPlayer(target);
        player.getInventory().addItem(VoucherItem.getVoucherItem(level));
        sender.sendMessage(Voucher.getInstance().getLang().getText("voucherGiven")
                .replace("%player%", target)
                .replace("%level%", amount));
        player.sendMessage(Voucher.getInstance().getLang().getText("voucherReceived")
                .replace("%level%", amount));
    }

    /**
     * Checks value is numeric
     *
     * @param strNum to parse
     * @return status of input numeric or not
     */
    public static boolean isNumeric(String strNum) {
        try {
            Integer.parseInt(strNum);
            return true;
        }
        catch (NumberFormatException nfe) {return false;}
    }
}
