package xyz.geik.farmer.modules.autoseller;

import lombok.Getter;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.modules.FarmerModule;

import java.util.ArrayList;
import java.util.List;

/**
 * AutoSeller module main class
 * @author Geik
 */
@Getter
public class AutoSeller extends FarmerModule {

    /**
     * Constructor of class
     */
    public AutoSeller() {}

    /**
     * -- GETTER --
     *  Get instance of the module
     *
     */
    @Getter
    private static AutoSeller instance;

    /**
     * Allowed items for auto sell
     */
    private List<String> allowedItems = new ArrayList<>();

    /**
     * Perm for auto sell
     */
    private String customPerm = "farmer.autoseller";

    /**
     * Default status of auto sell
     */
    private boolean defaultStatus = false;

    /**
     * onLoad method of module
     */
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

    /**
     * onEnable method of module
     */
    @Override
    public void onEnable() {
        setHasGui(true);
        getAllowedItems().addAll(getConfig().getStringList("items"));
        setLang(Main.getConfigFile().getSettings().getLang(), Main.getInstance());
        customPerm = getConfig().getString("settings.customPerm");
        defaultStatus = getConfig().getBoolean("settings.defaultStatus");
        registerListener(new AutoSellerEvent());
        registerListener(new AutoSellerGuiCreateEvent());
    }

    /**
     * onReload method of module
     */
    @Override
    public void onReload() {
        if (!this.isEnabled())
            return;
        if (!getAllowedItems().isEmpty())
            getAllowedItems().clear();
        getAllowedItems().addAll(getConfig().getStringList("items"));
        customPerm = getConfig().getString("settings.customPerm");
        defaultStatus = getConfig().getBoolean("settings.defaultStatus");
    }

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {

    }
}
