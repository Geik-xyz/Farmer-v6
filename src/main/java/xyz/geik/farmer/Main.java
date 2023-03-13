package xyz.geik.farmer;

import de.leonhard.storage.Config;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.commands.Commands;
import xyz.geik.farmer.commands.FarmerTabComplete;
import xyz.geik.farmer.database.DBQueries;
import xyz.geik.farmer.helpers.ItemsLoader;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.integrations.Integrations;
import xyz.geik.farmer.listeners.ListenerRegister;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.farmer.modules.autoharvest.AutoHarvest;
import xyz.geik.farmer.modules.autoseller.AutoSeller;
import xyz.geik.farmer.modules.production.Production;
import xyz.geik.farmer.modules.spawnerkiller.SpawnerKiller;
import xyz.geik.farmer.modules.voucher.Voucher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Main class of farmer
 * There is only loads, apis and
 * startup task codes.
 */
@Getter
public class Main extends JavaPlugin {

    // Register module to this class
    public Map<FarmerModule, Listener> listenerList = new HashMap<>();

    /**
     * Instance of this class
     */
    private static Main instance;

    /**
     * Config files which using SimplixStorage API for it.
     * Also, you can find usage code of API on helpers#StorageAPI
     */
    private static Config configFile, itemsFile, langFile;

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
        configFile = FarmerAPI.getStorageManager().initConfig("config");
        itemsFile = FarmerAPI.getStorageManager().initConfig("items");
        langFile = FarmerAPI.getStorageManager().initLangFile(getConfigFile().getString("settings.lang"));
    }

    /**
     * onEnable method calls from spigot api.
     * This is sort of the main(String... args) method.
     */
    public void onEnable() {
        // API Installer
        FarmerAPI.getFarmerManager();
        FarmerAPI.getModuleManager();
        FarmerAPI.getStorageManager();
        FarmerAPI.getDatabaseManager();
        setupEconomy();
        Settings.regenSettings();
        new ItemsLoader();
        FarmerLevel.loadLevels();
        getCommand("farmer").setExecutor(new Commands());
        getCommand("farmer").setTabCompleter(new FarmerTabComplete());
        DBQueries.createTable();
        DBQueries.loadAllFarmers();
        Integrations.registerIntegrations();
        sendEnableMessage();
        new ListenerRegister();
        loadMetrics();
        registerModules();
    }

    /**
     * disable method calls from spigot api.
     * executing it right before close.
     *
     * async tasks can be fail because server
     * can't handle async tasks while shutting down
     */
    public void onDisable() {
        DBQueries.updateAllFarmers();
    }

    /**
     * Getter for files, instance, integration, farmers, economy.
     */
    public static Config getConfigFile() { return configFile; }
    public static Config getItemsFile() { return itemsFile; }
    public static Config getLangFile() { return langFile; }
    public static Main getInstance() { return instance; }
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
     * Sending enable message to console.
     */
    private static void sendEnableMessage() {
        Bukkit.getConsoleSender().sendMessage(Main.color("&6&l		FARMER 		&b"));
        Bukkit.getConsoleSender().sendMessage(Main.color("&aDeveloped by &2Geik"));
        Bukkit.getConsoleSender().sendMessage(Main.color("&aDiscord: &2https://discord.geik.xyz"));
        Bukkit.getConsoleSender().sendMessage(Main.color("&aWeb: &2https://geik.xyz"));
    }

    /**
     * Custom charted metrics loader
     */
    private void loadMetrics() {
        Metrics metrics = new Metrics(Main.instance, 9646);
        metrics.addCustomChart(new Metrics.SingleLineChart("ciftci_sayisi", new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return FarmerAPI.getFarmerManager().getFarmers().size();
            }
        }));
        metrics.addCustomChart(new Metrics.SimplePie("api_eklentisi", new Callable<String>() {
            @Override
            public String call() {
                String[] data = getIntegration().getClass().getName().split(".");
                return data[data.length-1];
            }
        }));
    }

    /**
     * Register modules to this plugin
     */
    private void registerModules() {
        FarmerAPI.getModuleManager().registerModule(new Voucher());
        FarmerAPI.getModuleManager().registerModule(new Production());
        FarmerAPI.getModuleManager().registerModule(new AutoHarvest());
        FarmerAPI.getModuleManager().registerModule(new AutoSeller());
        FarmerAPI.getModuleManager().registerModule(new SpawnerKiller());
        FarmerAPI.getModuleManager().loadModules();
    }
}
