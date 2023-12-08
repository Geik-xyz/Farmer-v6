package xyz.geik.farmer.modules.autoharvest;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.farmer.modules.autoharvest.handlers.AutoHarvestEvent;
import xyz.geik.farmer.modules.autoharvest.handlers.AutoHarvestGuiCreateEvent;
import xyz.geik.glib.shades.xseries.XMaterial;

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

    @Getter
    private static AutoHarvest instance;

    private static AutoHarvestEvent autoHarvestEvent;

    private static AutoHarvestGuiCreateEvent autoHarvestGuiCreateEvent;

    private boolean requirePiston = false, checkAllDirections = false, withoutFarmer = false, checkStock = true, defaultStatus = false;

    private String customPerm = "farmer.autoharvest";

    private List<String> crops = new ArrayList<>();

    /**
     * onEnable method of module
     */
    @Override
    public void onEnable() {
        instance = this;
        if (!Main.getModulesFile().getVoucher().isStatus())
            this.setEnabled(false);
        this.setLang(Main.getConfigFile().getSettings().getLang(), Main.getInstance());
        this.setHasGui(true);
        autoHarvestEvent = new AutoHarvestEvent();
        autoHarvestGuiCreateEvent = new AutoHarvestGuiCreateEvent();
        Bukkit.getPluginManager().registerEvents(autoHarvestEvent, Main.getInstance());
        Bukkit.getPluginManager().registerEvents(autoHarvestGuiCreateEvent, Main.getInstance());
        getCrops().addAll(Main.getModulesFile().getAutoHarvest().getItems());
        requirePiston = Main.getModulesFile().getAutoHarvest().isRequirePiston();
        checkAllDirections = Main.getModulesFile().getAutoHarvest().isCheckAllDirections();
        withoutFarmer = Main.getModulesFile().getAutoHarvest().isWithoutFarmer();
        checkStock = Main.getModulesFile().getAutoHarvest().isCheckStock();
        customPerm = Main.getModulesFile().getAutoHarvest().getCustomPerm();
        defaultStatus = Main.getModulesFile().getAutoHarvest().isDefaultStatus();
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
        getCrops().addAll(Main.getModulesFile().getAutoHarvest().getItems());
        requirePiston = Main.getModulesFile().getAutoHarvest().isRequirePiston();
        checkAllDirections = Main.getModulesFile().getAutoHarvest().isCheckAllDirections();
        withoutFarmer = Main.getModulesFile().getAutoHarvest().isWithoutFarmer();
        checkStock = Main.getModulesFile().getAutoHarvest().isCheckStock();
        customPerm = Main.getModulesFile().getAutoHarvest().getCustomPerm();
        defaultStatus = Main.getModulesFile().getAutoHarvest().isDefaultStatus();
    }

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {
        HandlerList.unregisterAll(autoHarvestEvent);
        HandlerList.unregisterAll(autoHarvestGuiCreateEvent);
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
