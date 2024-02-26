package xyz.geik.farmer.modules.voucher;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.farmer.modules.voucher.commands.VoucherCommand;
import xyz.geik.farmer.modules.voucher.handlers.VoucherEvent;
import xyz.geik.farmer.shades.storage.Config;

/**
 * Voucher module main class
 */
@Getter
public class Voucher extends FarmerModule {

    /**
     * Constructor of class
     */
    public Voucher() {}

    @Getter
    private static Voucher instance;

    private static VoucherEvent voucherEvent;

    private Config langFile;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        instance = this;
        this.setLang(Main.getConfigFile().getSettings().getLang(), Main.getInstance());
        voucherEvent = new VoucherEvent();
        Bukkit.getPluginManager().registerEvents(voucherEvent, Main.getInstance());
        Main.getCommandManager().registerCommand(new VoucherCommand());
    }

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {
        HandlerList.unregisterAll(voucherEvent);
        Main.getCommandManager().unregisterCommand(new VoucherCommand());
    }
}
