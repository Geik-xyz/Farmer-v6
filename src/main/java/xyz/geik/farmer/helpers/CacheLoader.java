package xyz.geik.farmer.helpers;

import org.bukkit.Bukkit;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.handlers.FarmerPreLoadItemEvent;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.model.inventory.FarmerInv;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.farmer.shades.storage.Config;
import xyz.geik.glib.shades.xseries.XMaterial;

/**
 * Loads items from items.yml to ingame cache
 *
 * @author Geik
 */
public class CacheLoader {

    /**
     * Adds items to FarmerInv#defaultItems
     */
    public static void loadAllItems() {
        // Makes empty default items list
        if (!FarmerInv.defaultItems.isEmpty())
            FarmerInv.defaultItems.clear();

        // Loops Items in items.yml
        Main.getItemsFile().singleLayerKeySet("Items").forEach(key -> {
            Config config = Main.getItemsFile();
            if (!XMaterial.matchXMaterial(key).isPresent()) {
                Bukkit.getConsoleSender().sendMessage("§c[Farmer] §7Item §e" + key + " §7is not valid material!");
                return;
            }
            double price = config.contains("Items." + key + ".price") ? config.getDouble("Items." + key + ".price") : -1.0;
            FarmerItem defaultItem = new FarmerItem(key, price, 0);
            FarmerPreLoadItemEvent loadingEvent = new FarmerPreLoadItemEvent(defaultItem);
            Bukkit.getPluginManager().callEvent(loadingEvent);
            FarmerInv.defaultItems.add(loadingEvent.getFarmerItem());
        });
    }

    /**
     * Loads all levels of farmer and stats
     */
    public static void loadAllLevels() {
        if (!FarmerLevel.getAllLevels().isEmpty())
            FarmerLevel.getAllLevels().clear();

        Config config = Main.getLevelFile();
        config.singleLayerKeySet("levels").forEach(key -> {
            long capacity = config.getLong("levels." + key + ".capacity");
            long reqMoney = (config.contains("levels." + key + ".reqMoney")) ?
                    config.getLong("levels." + key + ".reqMoney") : 0;
            double tax = (config.contains("levels." + key + ".tax")) ?
                    config.getDouble("levels." + key + ".tax") : Main.getConfigFile().getTax().getRate();
            String perm = (config.contains("levels." + key + ".reqPerm")) ?
                    config.getString("levels." + key + ".reqPerm") : null;
            FarmerLevel level = new FarmerLevel(key, capacity, reqMoney, tax, perm);
            FarmerLevel.getAllLevels().add(level);
        });
    }

    /**
     * Main constructor of class
     */
    public CacheLoader() {}

}
