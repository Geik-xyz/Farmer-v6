package xyz.geik.farmer.modules.autoseller;

import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.modules.FarmerModule;

/**
 * AutoSeller module main class
 * @author Geik
 */
public class AutoSeller extends FarmerModule {

    private static AutoSeller instance;

    /**
     * Get instance of the module
     *
     * @return
     */
    public static AutoSeller getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        setName("AutoSeller");
        setDescription("Automatically sells items when capacity full");
        setModulePrefix("AutoSeller");
        setConfig(Main.getInstance());
        instance = this;
        if (!getConfig().getBoolean("settings.feature"))
            setEnabled(false);
    }

    @Override
    public void onEnable() {
        registerListener(new AutoSellerEvent());
        setLang(Settings.lang, Main.getInstance());
    }

    @Override
    public void onDisable() {

    }
}
