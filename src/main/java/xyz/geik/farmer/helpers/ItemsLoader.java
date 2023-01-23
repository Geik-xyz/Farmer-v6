package xyz.geik.farmer.helpers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.inventory.FarmerInv;
import xyz.geik.farmer.model.inventory.FarmerItem;

public class ItemsLoader {

    public ItemsLoader() {
        if (!FarmerInv.defaultItems.isEmpty())
            FarmerInv.defaultItems.clear();

        Main.getItemsFile().singleLayerKeySet("Items").stream().forEach(key -> {
            try {
                String checkKey = (key.contains("-")) ? key.split("-")[0] : key;
                Material.getMaterial(checkKey);
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
