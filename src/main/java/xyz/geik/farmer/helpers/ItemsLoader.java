package xyz.geik.farmer.helpers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.inventory.FarmerInv;
import xyz.geik.farmer.model.inventory.FarmerItem;

/**
 * Loads items from items.yml to ingame cache
 */
public class ItemsLoader {

    /**
     * Main constructor class which adds items to FarmerInv#defaultItems
     */
    public ItemsLoader() {
        // Makes empty default items list
        if (!FarmerInv.defaultItems.isEmpty())
            FarmerInv.defaultItems.clear();

        // Loops Items in items.yml
        Main.getItemsFile().singleLayerKeySet("Items").stream().forEach(key -> {
            try {
                // Checks material if it valid
                String checkKey = (key.contains("-")) ? key.split("-")[0] : key;
                Material.getMaterial(checkKey);
                // Price of item
                double price = Main.getItemsFile().getDouble("Items." + key + ".price");
                FarmerItem defaultItem = new FarmerItem(key, price, 0);
                FarmerInv.defaultItems.add(defaultItem);
            }
            catch (Exception e1) {
                Bukkit.getConsoleSender().sendMessage(Main.color("&4Material isn't correct. " + key));
            }
        });
    }

}
