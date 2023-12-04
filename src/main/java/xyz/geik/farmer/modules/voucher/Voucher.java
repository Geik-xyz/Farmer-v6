package xyz.geik.farmer.modules.voucher;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.modules.FarmerModule;
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
     * onLoad method of module
     */
    public void onLoad() {
        instance = this;
    }

    /**
     * onEnable method of module
     */
    public void onEnable() {
        this.setLang(Main.getConfigFile().getSettings().getLang(), Main.getInstance());
        voucherEvent = new VoucherEvent();
        Bukkit.getPluginManager().registerEvents(voucherEvent, Main.getInstance());
        overrideFarmer = getConfig().get("settings.useWhenFarmerExist", false);
        giveVoucherWhenRemove = getConfig().get("settings.giveVoucherWhenRemove", false);
    }

    /**
     * onReload method of module
     */
    public void onReload() {
        if (!this.isEnabled())
            return;
        overrideFarmer = getConfig().get("settings.useWhenFarmerExist", false);
        giveVoucherWhenRemove = getConfig().get("settings.giveVoucherWhenRemove", false);
    }

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {
        HandlerList.unregisterAll(voucherEvent);
    }
}
