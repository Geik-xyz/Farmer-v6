package xyz.geik.farmer.modules.autoharvest;

import xyz.geik.farmer.Main;
import xyz.geik.farmer.modules.FarmerModule;

/**
 * AutoHarvest module main class
 */
public class AutoHarvest extends FarmerModule {

    // Instance of the module
    private static AutoHarvest instance;

    /**
     * Get instance of the module
     *
     * @return
     */
    public static AutoHarvest getInstance() {
        return instance;
    }

    /**
     * Load module
     */
    @Override
    public void onLoad() {
        this.setName("AutoHarvest");
        this.setDescription("Automatically harvests crops");
        this.setModulePrefix("AutoHarvest");
        this.setConfig(Main.getInstance());
        instance = this;
        if (!getConfig().getBoolean("settings.feature"))
            this.setEnabled(false);
    }

    /**
     * Enable module
     */
    @Override
    public void onEnable() {
        registerListener(new AutoHarvestEvent());
    }

    @Override
    public void onDisable() {

    }
}
