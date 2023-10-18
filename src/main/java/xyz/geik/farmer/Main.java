package xyz.geik.farmer;

import de.leonhard.storage.Config;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.commands.Commands;
import xyz.geik.farmer.commands.FarmerTabComplete;
import xyz.geik.farmer.database.MySQL;
import xyz.geik.farmer.database.SQL;
import xyz.geik.farmer.database.SQLite;
import xyz.geik.farmer.helpers.ItemsLoader;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.integrations.EconomyIntegrations;
import xyz.geik.farmer.integrations.Integrations;
import xyz.geik.farmer.integrations.placeholderapi.PlaceholderAPI;
import xyz.geik.farmer.listeners.ListenerRegister;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.farmer.modules.autoharvest.AutoHarvest;
import xyz.geik.farmer.modules.autoseller.AutoSeller;
import xyz.geik.farmer.modules.production.Production;
import xyz.geik.farmer.modules.spawnerkiller.SpawnerKiller;
import xyz.geik.farmer.modules.voucher.Voucher;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main class of farmer
 * There is only loads, apis and
 * startup task codes.
 */
@Getter
public class Main extends JavaPlugin {

    /**
     * Listener list of modules
     */
    public Map<FarmerModule, Listener> listenerList = new HashMap<>();

    /**
     * SQL Manager
     */
    @Getter
    private SQL sql;

    @Getter
    @Setter
    private PlaceholderAPI placeholderAPI;

    /**
     * Instance of this class
     */
    @Getter
    private static Main instance;

    /**
     * Config files which using SimplixStorage API for it.
     * Also, you can find usage code of API on helpers#StorageAPI
     */
    @Getter
    private static Config itemsFile;

    /**
     * Lang file of plugin
     */
    @Getter
    private static Config langFile;

    /**
     * Database file of plugin
     */
    @Getter
    private static Config databaseFile;

    /**
     * Config file of plugin
     */
    @Getter
    private static Config configFile;

    /**
     * Main integration of plugin integrations#Integrations
     */
    @Getter
    private static Integrations integration;

    /**
     * Economy integration of plugin integrations#EconomyIntegrations
     */
    @Getter
    private static EconomyIntegrations economyIntegrations;

    /**
     * Constructor of class
     */
    public Main() {}


    /**
     * Loading files before enable
     */
    public void onLoad() {
        instance = this;
        configFile = FarmerAPI.getStorageManager().initConfig("config");
        itemsFile = FarmerAPI.getStorageManager().initConfig("items");
        langFile = FarmerAPI.getStorageManager().initLangFile(getConfigFile().getString("settings.lang"));
        databaseFile = FarmerAPI.getStorageManager().initConfig("storage/database");
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
        Settings.regenSettings();
        new ItemsLoader();
        FarmerLevel.loadLevels();
        getCommand("farmer").setExecutor(new Commands());
        getCommand("farmer").setTabCompleter(new FarmerTabComplete());
        Integrations.registerIntegrations();
        EconomyIntegrations.registerIntegrations();
        sendEnableMessage();
        setDatabaseManager();
        this.sql.loadAllFarmers();
        new ListenerRegister();
        loadMetrics();
        registerModules();
    }

    /**
     * disable method calls from spigot api.
     * executing it right before close.
     * async tasks can be fail because server
     * can't handle async tasks while shutting down
     */
    public void onDisable() {
        this.sql.updateAllFarmers();
        this.placeholderAPI.unregister();
    }

    /**
     * Integration setter
     *
     * @param data data of integration
     */
    public static void setIntegration(Integrations data) {
        integration = data;
    }

    /**
     * Economy integration setter
     *
     * @param data data of economy integration
     */
    public static void setEconomyIntegrations(EconomyIntegrations data) {
        economyIntegrations = data;
    }

    /**
     * Basic color translate method which changes minecraft color code to known one
     * @param text String of message
     * @return String of replaced text
     */
    public static @NotNull String color(String text) {
        final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        if (Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18") || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.20")) {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String color = text.substring(matcher.start(), matcher.end());
                text = text.replace(color, ChatColor.of(color) + "");
                matcher = pattern.matcher(text);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Sends enable message to console.
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
        metrics.addCustomChart(new Metrics.SingleLineChart("ciftci_sayisi", () -> FarmerManager.getFarmers().size()));
        metrics.addCustomChart(new Metrics.SimplePie("api_eklentisi", () -> {
            String[] data = getIntegration().getClass().getName().split(".");
            return data[data.length-1];
        }));
    }

    /**
     * Registering Database Manager
     */
    private void setDatabaseManager() {
        String sqlType = getDatabaseFile().getString("database.type");
        sqlType = sqlType.toLowerCase();

        if (sqlType.equals("sqlite")) {
            this.sql = new SQLite();
        } else if (sqlType.equals("mysql")) {
            this.sql = new MySQL();
        }
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
