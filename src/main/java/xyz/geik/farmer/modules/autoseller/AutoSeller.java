package xyz.geik.farmer.modules.autoseller;

import lombok.Getter;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.modules.FarmerModule;

import java.util.ArrayList;
import java.util.List;

/**
 * AutoSeller module main class
 * @author Geik
 */
@Getter
public class AutoSeller extends FarmerModule {

    private static AutoSeller instance;

    private List<String> allowedItems = new ArrayList<>();

    private String customPerm = "farmer.autoseller";

    private boolean defaultStatus = false;

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
        setHasGui(true);
        getAllowedItems().addAll(getConfig().getStringList("items"));
        setLang(Settings.lang, Main.getInstance());
        customPerm = getConfig().getString("settings.customPerm");
        defaultStatus = getConfig().getBoolean("settings.defaultStatus");
        registerListener(new AutoSellerEvent());
        registerListener(new AutoSellerGuiCreateEvent());
    }

    @Override
    public void onDisable() {

    }
}
