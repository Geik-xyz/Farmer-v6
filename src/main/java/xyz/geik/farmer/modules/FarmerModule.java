package xyz.geik.farmer.modules;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.modules.production.Production;
import xyz.geik.farmer.modules.voucher.Voucher;

/**
 * Module system of Farmer
 * You can extend a class with
 * this class and register it
 *
 * @author Geyik
 */
@Getter
@Setter
public abstract class FarmerModule {

    // Module name
    protected String name;

    // Module enabled or not
    protected boolean isEnabled;

    // Module description
    protected String description;

    // Module prefix
    protected String modulePrefix;

    /**
     * FarmerModule constructor
     *
     * @param name
     * @param isEnabled
     * @param description
     * @param modulePrefix
     */
    public FarmerModule(String name, boolean isEnabled, String description, String modulePrefix) {
        this.name = name;
        this.isEnabled = isEnabled;
        this.description = description;
        this.modulePrefix = modulePrefix;
    }

    /**
     * Register listeners to this plugin
     */
    public abstract void registerListeners();

    /**
     * When enable the module this method will be called
     */
    public abstract void onEnable();

    /**
     * Register listener to this plugin
     *
     * @param listener
     */
    public void registerListener(Listener listener) {
        PluginManager pm = Main.getInstance().getServer().getPluginManager();
        Main.getInstance().getListenerList().put(this, listener);
        pm.registerEvents(listener, Main.getInstance());
        System.out.println(" [LISTENERS] " + listener.getClass().getSimpleName() + " was registered");
    }

    /**
     * Register modules to this plugin
     */
    public static void registerModules() {
        ModuleManager moduleManager = Main.getInstance().getModuleManager();
        moduleManager.registerModule(new Voucher());
        moduleManager.registerModule(new Production());
        moduleManager.loadModules();
    }
}