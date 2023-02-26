package xyz.geik.farmer.modules;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.modules.exceptions.ReloadModuleException;

import java.util.ArrayList;
import java.util.List;

/**
 * Module Manager
 *
 * @author Geyik
 */
public class ModuleManager {

    // Module list of Farmer
    protected List<FarmerModule> farmerModuleList = new ArrayList<>();

    // Getter method of module list
    public List<FarmerModule> getModuleList() {
        return farmerModuleList;
    }

    /**
     * Register module
     * @param farmerModule
     */
    public void registerModule(FarmerModule farmerModule) {
        farmerModuleList.add(farmerModule);
    }

    /**
     * Remove module
     * @param farmerModule
     */
    public void removeModule(FarmerModule farmerModule) {
        farmerModuleList.remove(farmerModule);
    }

    /**
     * Load modules
     */
    public void loadModules() {
        for (FarmerModule farmerModule : Main.getInstance().getModuleManager().getModuleList()) {
            if (!farmerModule.isEnabled())
                Bukkit.broadcastMessage(Main.color("&4[FarmerModules] &c" + farmerModule.getName() + " disabled"));
            else {
                Bukkit.broadcastMessage(Main.color("&2[FarmerModules] &a" + farmerModule.getName() + " enabled"));
                farmerModule.onEnable();
                farmerModule.registerListeners();
            }
        }
    }

    /**
     * Reload module
     *
     * @param farmerModule
     * @throws ReloadModuleException
     */
    public void reloadModule(FarmerModule farmerModule) throws ReloadModuleException {
        getModuleList().remove(farmerModule);
        close(farmerModule);
        try {
            registerModule(farmerModule.getClass().newInstance());
        } catch (Exception e) {
            throw new ReloadModuleException(e.getMessage());
        }
        System.out.println(farmerModule.getName() + " was reloaded. It is: " + farmerModule.isEnabled);
    }


    /**
     * Gets module by name which is registered in Farmer
     *
     * @param module
     * @return
     */
    public FarmerModule getByName(String module) {
        FarmerModule farmerModuleSelected = null;
        for (FarmerModule mods : getModuleList()) {
            if (mods.getName().equals(module)) {
                farmerModuleSelected = mods;
                return farmerModuleSelected;
            }
        }
        throw new NullPointerException("Module cannot be found");
    }

    /**
     * Close module
     * @param farmerModule
     */
    public void close(FarmerModule farmerModule) {
        HandlerList handlerList = new HandlerList();
        handlerList.unregister(Main.getInstance().getListenerList().get(farmerModule));
        Main.getInstance().getListenerList().remove(farmerModule);
    }
}