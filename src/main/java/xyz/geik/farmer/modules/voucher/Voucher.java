package xyz.geik.farmer.modules.voucher;

import lombok.Getter;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.modules.FarmerModule;

/**
 * Voucher module main class
 */
@Getter
public class Voucher extends FarmerModule {

    // Instance of the module
    private static Voucher instance;

    public static Voucher getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        this.setName("Voucher");
        this.setEnabled(true);
        this.setDescription("Voucher module");
        this.setModulePrefix("Voucher");
        instance = this;
        this.setConfig(Main.getInstance());
        this.setLang(Settings.lang, Main.getInstance());
        if (!getConfig().getBoolean("settings.feature"))
            this.setEnabled(false);
    }

    @Override
    public void registerListeners() {
        registerListener(new VoucherEvent());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }
}
