package xyz.geik.farmer.helpers;

import xyz.geik.farmer.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Default settings set and
 * there is a method which
 * updates these settings.
 */
public class Settings {

    /**
     * Constructor of class
     */
    public Settings() {}

    /**
     * ignores player drop configuration
     */
    public static boolean ignorePlayerDrop = false,
    /**
     * deposit tax configuration
     */
            depositTax = false,
    /**
     * autoCreateFarmer configuration
     */
            autoCreateFarmer = false,
    /**
     * buyFarmer configuration
     */
            buyFarmer = true;
    private static List<String> allowedWorlds = new ArrayList<>();

    /**
     * default tax value
     */
    public static double defaultTax = 20.0;
    /**
     * price of farmer
     */
    public static int farmerPrice = -1;
    /**
     * tax user who deposited to this account
     */
    public static String taxUser = "Geyik",
    /**
     * default language file
     */
            lang = "en";

    /**
     * is world allowed in farmer or not
     *
     * @param worldName name of world
     * @return boolean is allowed
     */
    public static boolean isWorldAllowed(String worldName) {
        if (allowedWorlds.isEmpty())
            return true;
        else
            return allowedWorlds.contains(worldName);
    }

    /**
     * Updates default settings from config
     */
    public static void regenSettings() {
        ignorePlayerDrop = Main.getConfigFile().getBoolean("settings.ignorePlayerDrop");
        if (Main.getConfigFile().contains("settings.allowedWorlds"))
            allowedWorlds = (List<String>) Main.getConfigFile().getList("settings.allowedWorlds");
        defaultTax = Main.getConfigFile().getDouble("tax.rate");
        farmerPrice = Main.getConfigFile().getInt("settings.farmerPrice");
        depositTax = Main.getConfigFile().getBoolean("tax.deposit");
        taxUser = Main.getConfigFile().getString("tax.depositUser");
        autoCreateFarmer = Main.getConfigFile().getBoolean("settings.autoCreateFarmer");
        buyFarmer = Main.getConfigFile().getBoolean("settings.buyFarmer");
        lang = Main.getConfigFile().getString("settings.lang");
    }

}
