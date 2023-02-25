package xyz.geik.farmer.modules.production;

import de.leonhard.storage.Config;
import lombok.Getter;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.helpers.StorageAPI;
import xyz.geik.farmer.modules.FarmerModule;

@Getter
public class Production extends FarmerModule {

    private Config config;
    private static Production instance;

    private String[] numberFormat = new String[]{"k", "m", "b", "t"};

    public static Production getInstance() {
        return instance;
    }

    public Production() {
        super("Production", true, "Average Production Calculating module", "Production Calculating");
        instance = this;
        config = new StorageAPI().initConfig("modules/production/config");
        if (!getConfig().getBoolean("settings.feature") || !Settings.hasAnyProductionCalculating)
            this.isEnabled = false;
    }

    @Override
    public void registerListeners() {
        registerListener(new ProductionCalculateEvent());
    }

    @Override
    public void onEnable() {
        numberFormat[0] = getConfig().getText("numberFormat.thousand");
        numberFormat[1] = getConfig().getText("numberFormat.million");
        numberFormat[2] = getConfig().getText("numberFormat.billion");
        numberFormat[3] = getConfig().getText("numberFormat.trillion");
    }
}
