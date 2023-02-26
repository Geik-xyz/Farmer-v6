package xyz.geik.farmer.modules.voucher;

import de.leonhard.storage.Config;
import lombok.Getter;
import xyz.geik.farmer.helpers.StorageAPI;
import xyz.geik.farmer.modules.FarmerModule;

/**
 * Voucher module main class
 */
@Getter
public class Voucher extends FarmerModule {

    // Config file of the module
    private Config config;

    // Instance of the module
    private static Voucher instance;

    public static Voucher getInstance() {
        return instance;
    }

    public Voucher() {
        super("Voucher", true, "Voucher module", "Voucher");
        instance = this;
        config = new StorageAPI().initConfig("modules/voucher/config");
        if (!getConfig().getBoolean("settings.feature")) {
            this.isEnabled = false;
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void registerListeners() {
        registerListener(new VoucherEvent());
    }
}
