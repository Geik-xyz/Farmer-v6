package xyz.geik.farmer.helpers;

import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import de.leonhard.storage.Config;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.model.inventory.FarmerInv;
import xyz.geik.farmer.model.inventory.FarmerItem;

import java.util.stream.Collectors;

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
            boolean hasProduction = config.getOrDefault("Items." + key + ".calculateProduction", false);
            FarmerItem defaultItem = new FarmerItem(key, price, 0, hasProduction);
            FarmerInv.defaultItems.add(defaultItem);
        });
        // Enables production calculation if any item has it
        Settings.hasAnyProductionCalculating = FarmerInv.defaultItems.stream().anyMatch(FarmerItem::hasProductCalculating);
    }

}
