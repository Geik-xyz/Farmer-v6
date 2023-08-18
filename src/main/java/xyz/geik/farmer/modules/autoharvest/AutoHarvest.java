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

    /**
     * Constructor of class
     */
    public AutoHarvest() {}

    private boolean requirePiston = false, checkAllDirections = false, withoutFarmer = false, checkStock = true, defaultStatus = false;

    /**
     * perm of farmer
     */
    private String customPerm = "farmer.autoharvest";

    /**
     * Crops of auto harvest
     */
    private List<String> crops = new ArrayList<>();

    @Getter
    private static AutoHarvest instance;

    /**
     * onLoad method of module
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
     * onEnable method of module
     */
    @Override
    public void onEnable() {
        setHasGui(true);
        getCrops().addAll(getConfig().getStringList("settings.items"));
        requirePiston = getConfig().getBoolean("settings.requirePiston");
        checkAllDirections = getConfig().getBoolean("settings.checkAllDirections");
        withoutFarmer = getConfig().getBoolean("settings.withoutFarmer");
        checkStock = getConfig().getBoolean("settings.checkStock");
        customPerm = getConfig().getString("settings.customPerm");
        defaultStatus = getConfig().getBoolean("settings.defaultStatus");
        registerListener(new AutoHarvestEvent());
        registerListener(new AutoHarvestGuiCreateEvent());
        setLang(Settings.lang, Main.getInstance());
    }

    /**
     * onReload method of module
     */
    @Override
    public void onReload() {
        if (!this.isEnabled())
            return;
        if (!getCrops().isEmpty())
            getCrops().clear();
        getCrops().addAll(getConfig().getStringList("settings.items"));
        requirePiston = getConfig().getBoolean("settings.requirePiston");
        checkAllDirections = getConfig().getBoolean("settings.checkAllDirections");
        withoutFarmer = getConfig().getBoolean("settings.withoutFarmer");
        checkStock = getConfig().getBoolean("settings.checkStock");
        customPerm = getConfig().getString("settings.customPerm");
        defaultStatus = getConfig().getBoolean("settings.defaultStatus");
    }

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {

    }

    /**
     * Checks if auto harvest collect this crop.
     *
     * @param material of crop
     * @return is crop can harvestable
     */
    public static boolean checkCrop(XMaterial material) {
        return getInstance().getCrops().stream().anyMatch(crop -> material.equals(XMaterial.valueOf(crop)));
    }
}
