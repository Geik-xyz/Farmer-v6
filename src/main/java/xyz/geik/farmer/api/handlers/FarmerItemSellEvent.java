package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerItem;

/**
 * FarmerItemSellEvent event fires when a player sells a FarmerItem
 * @see Farmer
 * @see FarmerItem
 * @author poyrazinan
 */
@Getter
public class FarmerItemSellEvent extends Event {

    /**
     * Farmer object which contains the FarmerItem
     * @see Farmer
     */
    private Farmer farmer;

    /**
     * FarmerItem object which is sold
     * @see FarmerItem
     */
    private FarmerItem farmerItem;

    /**
     * OfflinePlayer object which is the seller
     * @see OfflinePlayer
     */
    private final OfflinePlayer offlinePlayer;

    /**
     * FarmerItemSellEvent constructor with Farmer, FarmerItem and OfflinePlayer
     *
     * @param farmer Farmer object which contains the FarmerItem
     * @param farmerItem FarmerItem object which is sold
     * @param offlinePlayer OfflinePlayer object which is the seller
     */
    public FarmerItemSellEvent(Farmer farmer, FarmerItem farmerItem, OfflinePlayer offlinePlayer) {
        this.farmer = farmer;
        this.farmerItem = farmerItem;
        this.offlinePlayer = offlinePlayer;
    }

    /**
     * Spigot handlers requirements
     *
     * @return
     */
    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
