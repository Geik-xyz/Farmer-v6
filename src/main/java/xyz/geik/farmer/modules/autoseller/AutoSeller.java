package xyz.geik.farmer.modules.autoseller;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.farmer.modules.autoseller.handlers.AutoSellerEvent;
import xyz.geik.farmer.modules.autoseller.handlers.AutoSellerGuiCreateEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * AutoSeller module main class
 * @author Geik
 */
@Getter
public class AutoSeller extends FarmerModule {

    public AutoSeller() {}

    @Getter
    private static AutoSeller instance;

    private static AutoSellerEvent autoSellerEvent;

    private static AutoSellerGuiCreateEvent autoSellerGuiCreateEvent;

    private List<String> allowedItems = new ArrayList<>();

    private String customPerm = "farmer.autoseller";

    private boolean defaultStatus = false;

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
        autoSellerEvent = new AutoSellerEvent();
        autoSellerGuiCreateEvent = new AutoSellerGuiCreateEvent();
        Bukkit.getPluginManager().registerEvents(autoSellerEvent, Main.getInstance());
        Bukkit.getPluginManager().registerEvents(autoSellerGuiCreateEvent, Main.getInstance());
        getAllowedItems().addAll(Main.getModulesFile().getAutoSeller().getItems());
        customPerm = Main.getModulesFile().getAutoSeller().getCustomPerm();
        defaultStatus = Main.getModulesFile().getAutoSeller().isDefaultStatus();
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
        getAllowedItems().addAll(Main.getModulesFile().getAutoSeller().getItems());
        customPerm = Main.getModulesFile().getAutoSeller().getCustomPerm();
        defaultStatus = Main.getModulesFile().getAutoSeller().isDefaultStatus();
    }

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {
        HandlerList.unregisterAll(autoSellerEvent);
        HandlerList.unregisterAll(autoSellerGuiCreateEvent);
    }
}
