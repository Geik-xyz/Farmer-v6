package xyz.geik.farmer.modules;

import de.leonhard.storage.Config;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;

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

    /**
     * Name of module
     */
    private String name = "FarmerModule";

    /**
     * Situation of module
     */
    private boolean isEnabled = true, hasGui = false;

    /**
     * Module description
     */
    private String description = "FarmerModule description";

    /**
     * Module prefix used in console messages
     */
    private String modulePrefix = "FarmerModule";

    /**
     * Config of module
     */
    private Config config;

    /**
     * Lang of module
     */
    private Config lang;

    /**
     * FarmerModule constructor
     *
     */
    public FarmerModule() {}

    /**
     * When load the module this method will be called
     */
    public abstract void onLoad();

    /**
     * When enable the module this method will be called
     */
    public abstract void onEnable();

    /**
     * When disable the module this method will be called
     */
    public abstract void onDisable();

    /**
     * Set default config of plugin
     * You may use it in your onLoad
     * or onEnable method of your module
     *
     * @param plugin
     */
    public void setConfig(JavaPlugin plugin) {
        config = FarmerAPI.getStorageManager()
                .initConfig("modules/" + this.getName().toLowerCase() + "/config", plugin);
    }

    /**
     * Set default config of plugin
     * You may use it in your onLoad
     * or onEnable method of your module
     *
     * <p><b>IMPORTANT</b> file must be in
     * resources/lang folder, and it
     * must be named of lang as farmer.</p>
     *
     * @param langName
     * @param plugin
     */
    public void setLang(String langName, JavaPlugin plugin) {
        lang = FarmerAPI.getStorageManager()
                .initConfig("modules/" + this.getName().toLowerCase() + "/lang/" + langName, plugin);
    }

    /**
     * Register listener to this plugin
     *
     * @param listener
     */
    public void registerListener(Listener listener) {
        PluginManager pm = Main.getInstance().getServer().getPluginManager();
        Main.getInstance().getListenerList().put(this, listener);
        pm.registerEvents(listener, Main.getInstance());
        Bukkit.getConsoleSender().sendMessage(Main.color("&6[FarmerManager] &a" + listener.getClass().getSimpleName() + " was registered"));
    }
}