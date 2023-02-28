package xyz.geik.farmer.api.managers;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.farmer.modules.exceptions.ModuleExistException;
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
    private List<FarmerModule> farmerModuleList = new ArrayList<>();

    // Getter method of module list
    public List<FarmerModule> getModuleList() {
        return farmerModuleList;
    }

    /**
     * Register module registering only adds queue to load.
     * Use addModule to force load module.
     * This method is used for module load on startup.
     *
     * @param farmerModule
     */
    public void registerModule(@NotNull FarmerModule farmerModule) {
        farmerModule.onLoad();
        farmerModuleList.add(farmerModule);
    }

    /**
     * Add module after module load.
     * Force load module.
     *
     * @param farmerModule
     */
    public void addModule(FarmerModule farmerModule) throws ModuleExistException {
        if (getModuleList().contains(farmerModule))
            throw new ModuleExistException("Module " + farmerModule.getName() + " is already loaded!", farmerModule);
        else
            registerModule(farmerModule);
        loadModule(farmerModule);
    }

    /**
     * Remove module
     * @param farmerModule
     */
    public void removeModule(@NotNull FarmerModule farmerModule) {
        farmerModule.setEnabled(false);
        close(farmerModule);
        getModuleList().remove(farmerModule);
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
        Bukkit.getConsoleSender().sendMessage(Main.color("&2[FarmerModules] &a" + farmerModule.getName() +
                " was reloaded it is: " + farmerModule.isEnabled()));
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
    public void close(@NotNull FarmerModule farmerModule) {
        farmerModule.onDisable();
        HandlerList handlerList = new HandlerList();
        handlerList.unregister(Main.getInstance().getListenerList().get(farmerModule));
        Main.getInstance().getListenerList().remove(farmerModule);
    }

    /**
     * Load all modules in array list.
     */
    public void loadModules() {
        for (FarmerModule farmerModule : FarmerAPI.getModuleManager().getModuleList()) {
            loadModule(farmerModule);
        }
    }

    /**
     * Load modules handler method
     */
    private void loadModule(@NotNull FarmerModule farmerModule) {
        if (!farmerModule.isEnabled())
            Bukkit.getConsoleSender().sendMessage(Main.color("&4[FarmerModules] &c" + farmerModule.getName() + " disabled"));
        else {
            Bukkit.getConsoleSender().sendMessage(Main.color("&2[FarmerModules] &a" + farmerModule.getName() + " enabled"));
            farmerModule.onEnable();
        }
    }
}