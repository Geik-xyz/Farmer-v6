package xyz.geik.farmer.api;

import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.api.managers.ModuleManager;

/**
 * All the api configuration can be found here.
 * @author poyrazinan
 */
public class FarmerAPI {

    /**
     * Constructor of class
     */
    public FarmerAPI() {}

    /**
     * Returns instance of Main class
     * @return Main instance
     * @see xyz.geik.farmer.Main
     */
    public static Main getInstance() {
        return Main.getInstance();
    }

    private static FarmerManager farmerManager;

    /**
     * Farmer manager for farmer manipulations.
     * @return FarmerManager
     * @see xyz.geik.farmer.api.managers.FarmerManager
     */
    public static FarmerManager getFarmerManager() {
        if (farmerManager == null)
            farmerManager = new FarmerManager();
        return farmerManager;
    }

    private static ModuleManager moduleManager;

    /**
     * Module manager for module manipulations.
     * @return ModuleManager
     * @see xyz.geik.farmer.api.managers.ModuleManager
     */
    public static ModuleManager getModuleManager() {
        if (moduleManager == null)
            moduleManager = new ModuleManager();
        return moduleManager;
    }
}
