package xyz.geik.farmer.modules.autoseller;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.handlers.FarmerItemCollectEvent;
import xyz.geik.farmer.api.handlers.FarmerItemSellEvent;
import xyz.geik.farmer.api.handlers.FarmerStorageFullEvent;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerItem;

public class AutoSellerEvent implements Listener {

    /**
     * TODO Description
     * @param event
     */
    @EventHandler
    public void onAutoSellerEvent(@NotNull FarmerStorageFullEvent event) {
        Farmer farmer = event.getFarmer();
        if (FarmerAPI.getModuleManager().getAttributeStatus("autoseller", farmer)) {
            OfflinePlayer owner = Bukkit.getOfflinePlayer(farmer.getOwnerUUID());
            // Checks if the farmer is not true for default status
            // Checks if the farmer owner is online for perm check
            // Checks if the farmer owner has the custom perm
            // If all true then closes auto seller
            if (!AutoSeller.getInstance().isDefaultStatus()
                    && owner.isOnline()
                    && !owner.getPlayer().hasPermission(AutoSeller.getInstance().getCustomPerm())) {
                FarmerAPI.getModuleManager().changeAttribute("autoseller", farmer);
                return;
            }
            XMaterial material = XMaterial.matchXMaterial(event.getItem());
            FarmerItem farmerItem = farmer.getInv().getStockedItem(material);

            // Checks if auto seller allowed items contains the item
            // Or if the allowed items list is empty because all items allowed for this setting
            if (AutoSeller.getInstance().getAllowedItems().contains(farmerItem.getName())
                    || AutoSeller.getInstance().getAllowedItems().isEmpty()) {
                FarmerItemSellEvent itemSellEvent = new FarmerItemSellEvent(farmer, farmerItem, owner.getPlayer());
                Bukkit.getPluginManager().callEvent(itemSellEvent);

                FarmerItemCollectEvent collectEvent = new FarmerItemCollectEvent(farmer, event.getItem(), event.getLeftAmount(), event.getItemSpawnEvent());
                Bukkit.getPluginManager().callEvent(collectEvent);
            }
        }
    }
}
