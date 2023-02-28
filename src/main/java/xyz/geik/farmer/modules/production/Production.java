package xyz.geik.farmer.modules.production;

import lombok.Getter;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.modules.FarmerModule;

/**
 * Production module main class
 */
@Getter
public class Production extends FarmerModule {

    // Instance of module
    private static Production instance;

    private String[] numberFormat = new String[]{"k", "m", "b", "t"};

    /**
     * Get instance of module
     *
     * @return instance
     */
    public static Production getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        this.setName("Production");
        this.setEnabled(true);
        this.setDescription("Average Production Calculating module");
        this.setModulePrefix("Production Calculating");
        instance = this;
        this.setConfig(Main.getInstance());
        this.setLang(Settings.lang, Main.getInstance());
        if (!getConfig().getBoolean("settings.feature") || !Settings.hasAnyProductionCalculating)
            this.setEnabled(false);
    }

    @Override
    public void onEnable() {
        registerListener(new ProductionCalculateEvent());
        numberFormat[0] = getLang().getText("numberFormat.thousand");
        numberFormat[1] = getLang().getText("numberFormat.million");
        numberFormat[2] = getLang().getText("numberFormat.billion");
        numberFormat[3] = getLang().getText("numberFormat.trillion");
    }

    @Override
    public void onDisable() {

    }
}
