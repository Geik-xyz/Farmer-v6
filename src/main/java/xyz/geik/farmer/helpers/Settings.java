package xyz.geik.farmer.helpers;

import xyz.geik.farmer.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Default settings set and
 * there is a method which
 * updates this settings.
 */
public class Settings {

    public static boolean ignorePlayerDrop = false, depositTax = false, autoCreateFarmer = false, buyFarmer = true;
    private static List<String> allowedWorlds = new ArrayList<>();
    public static double defaultTax = 20.0;
    public static int farmerPrice = -1;
    public static String taxUser = "Geyik", lang = "en";

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
