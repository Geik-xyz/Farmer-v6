package xyz.geik.farmer.modules.autoharvest;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.block.BlockState;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.modules.FarmerModule;

import java.util.ArrayList;
import java.util.List;

/**
 * AutoHarvest module main class
 */
@Getter
public class AutoHarvest extends FarmerModule {

    private boolean requirePiston = false, checkAllDirections = false, withoutFarmer = false, checkStock = true;
    private String customPerm = "farmer.autoharvest";
    private List<String> crops = new ArrayList<>();

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
        setName("AutoHarvest");
        setDescription("Automatically harvests crops");
        setModulePrefix("AutoHarvest");
        setConfig(Main.getInstance());
        instance = this;
        if (!getConfig().getBoolean("settings.feature"))
            this.setEnabled(false);
    }

    /**
     * Enable module
     */
    @Override
    public void onEnable() {
        setHasGui(true);
        getCrops().addAll(getConfig().getStringList("settings.items"));
        requirePiston = getConfig().getBoolean("settings.requirePiston");
        checkAllDirections = getConfig().getBoolean("settings.checkAllDirections");
        withoutFarmer = getConfig().getBoolean("settings.withoutFarmer");
        checkStock = getConfig().getBoolean("settings.checkStock");
        registerListener(new AutoHarvestEvent());
        registerListener(new AutoHarvestGuiCreateEvent());
        setLang(Settings.lang, Main.getInstance());
    }

    @Override
    public void onDisable() {

    }

    /**
     * Checks if auto harvest collect this crop.
     *
     * @param state
     * @return
     */
    public static boolean checkCrop(XMaterial material) {
        return getInstance().getCrops().stream().anyMatch(crop -> material.equals(XMaterial.valueOf(crop)));
    }
}
