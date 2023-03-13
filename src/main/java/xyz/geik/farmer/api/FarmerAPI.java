package xyz.geik.farmer.api;

import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.managers.DatabaseManager;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.api.managers.ModuleManager;
import xyz.geik.farmer.api.managers.StorageManager;

/**
 * All the api configuration can be found here.
 */
public class FarmerAPI {

    /**
     * Returns instance of Main class
     * @return
     */
    public static Main getInstance() {
        return Main.getInstance();
    }

    private static StorageManager storageManager;

    /**
     * Storage manager for config and json files.
     */
    public static StorageManager getStorageManager() {
        if (storageManager == null)
            storageManager = new StorageManager();
        return storageManager;
    }

    private static FarmerManager farmerManager;

    /**
     * Farmer manager for farmer manipulations.
     * @return
     */
    public static FarmerManager getFarmerManager() {
        if (farmerManager == null)
            farmerManager = new FarmerManager();
        return farmerManager;
    }

    private static ModuleManager moduleManager;

    /**
     * Module manager for module manipulations.
     * @return
     */
    public static ModuleManager getModuleManager() {
        if (moduleManager == null)
            moduleManager = new ModuleManager();
        return moduleManager;
    }

    private static DatabaseManager databaseManager;

    /**
     * Database manager for database manipulations.
     * @return
     */
    public static DatabaseManager getDatabaseManager() {
        if (databaseManager == null)
            databaseManager = new DatabaseManager();
        return databaseManager;
    }
}
