package xyz.geik.farmer;

import de.leonhard.storage.Config;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.commands.Commands;
import xyz.geik.farmer.commands.FarmerTabComplete;
import xyz.geik.farmer.database.DBQueries;
import xyz.geik.farmer.helpers.ItemsLoader;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.helpers.StorageAPI;
import xyz.geik.farmer.integrations.Integrations;
import xyz.geik.farmer.listeners.ListenerRegister;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.FarmerLevel;

import java.util.HashMap;

/**
 * Main class of farmer
 * There is only loads, apis and
 * startup task codes.
 */
public class Main extends JavaPlugin{

    /**
     * Instance of this class
     */
    private static Main instance;

    /**
     * Config files which using SimplixStorage API for it.
     * Also you can find usage code of API on helpers#StorageAPI
     */
    private static Config configFile, itemsFile, langFile;

    /**
     * Loaded farmer cache.
     */
    private static HashMap<String, Farmer> farmers = new HashMap<String, Farmer>();

    /**
     * Main integration of plugin integrations#Integrations
     */
    private static Integrations integration;

    /**
     * Economy hookup it's initialing down below.
     * #setupEconomy
     */
    private static Economy econ = null;


    /**
     * Loading files before enable
     */
    public void onLoad() {
        instance = this;
        configFile = new StorageAPI().initConfig("config");
        itemsFile = new StorageAPI().initConfig("items");
        langFile = new StorageAPI().initConfig("lang/" + getConfigFile().getString("settings.lang"));
    }

    /**
     * onEnable method calls from spigot api.
     * This is sort of the main(String... args) method.
     */
    public void onEnable() {
        setupEconomy();
        Settings.regenSettings();
        new ItemsLoader();
        FarmerLevel.loadLevels();
        new ListenerRegister();
        getCommand("farmer").setExecutor(new Commands());
        getCommand("farmer").setTabCompleter(new FarmerTabComplete());
        DBQueries.createTable();
        DBQueries.loadAllFarmers();
        Integrations.registerIntegrations();
    }

    /**
     * disable method calls from spigot api.
     * executing it right before close.
     *
     * async tasks can be fail because server
     * can't handle async tasks while shutting down
     */
    public void onDisable() {
        // TODO save farmers
        DBQueries.updateAllFarmers();
    }

    /**
     * Getter for files, instance, integration, farmers, economy.
     */
    public static Config getConfigFile() { return configFile; }
    public static Config getItemsFile() { return itemsFile; }
    public static Config getLangFile() { return langFile; }
    public static Main getInstance() { return instance; }
    public static HashMap<String, Farmer> getFarmers() { return farmers; }
    public static Integrations getIntegration() { return integration; }
    public static Economy getEcon() { return econ; }

    /**
     * Integration setter
     *
     * @param data
     */
    public static void setIntegration(Integrations data) {
        integration = data;
    }

    /**
     * Basic color translate method which changing '&' to '§'
     *
     * Because minecraft using '§' for color example: '§a Hello World!'
     * But it's hard to write so traditionally everyone using '&' for
     * color codes. This method converting '&' to '§'.
     * @param text
     * @return
     */
    public static @NotNull String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Setup economy by Vault.
     * @return
     */
    private boolean setupEconomy() {
        if (Main.instance.getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = Main.instance.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = rsp.getProvider();
        return econ != null;
    }

    /**
     * Getting NMS Version of server.
     *
     * 1_18_R2, 1_8_R01 etc.
     *
     * Created this method for 1.8+ and 1.12- integration.
     * Also check method down below.
     *
     * @return
     */
    public static @NotNull String getNMSVersion() {
        String v = Bukkit.getServer().getClass().getPackage().getName();
        return v.substring(v.lastIndexOf('.') + 1);
    }

    /**
     * Using this for old version integration.
     * And it detects version of server by #getNMSVersion method
     * then checking if it old or not.
     *
     * @return
     */
    public static boolean isOldVersion() {
        String nmsVer = getNMSVersion();
        if (nmsVer.contains("1_7") || nmsVer.contains("1_8") || nmsVer.contains("1_9")
            || nmsVer.contains("1_10") || nmsVer.contains("1_11") || nmsVer.contains("1_12"))
            return true;
        else return false;
    }
}
