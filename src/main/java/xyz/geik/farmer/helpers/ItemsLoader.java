package xyz.geik.farmer.helpers;

import com.cryptomorin.xseries.XMaterial;
import de.leonhard.storage.Config;
import org.bukkit.Bukkit;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.inventory.FarmerInv;
import xyz.geik.farmer.model.inventory.FarmerItem;

/**
 * Loads items from items.yml to ingame cache
 *
 * @author Geik
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
            Config config = Main.getItemsFile();
            if (!XMaterial.matchXMaterial(key).isPresent()) {
                Bukkit.getConsoleSender().sendMessage("§c[Farmer] §7Item §e" + key + " §7is not valid material!");
                return;
            }
            double price = config.getDouble("Items." + key + ".price");
            FarmerItem defaultItem = new FarmerItem(key, price, 0);
            FarmerInv.defaultItems.add(defaultItem);
        });
    }

}
