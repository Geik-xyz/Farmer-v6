package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import lombok.Setter;
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
    private final Farmer farmer;

    /**
     * FarmerItem object which is sold
     * @see FarmerItem
     */
    private final FarmerItem farmerItem;

    /**
     * OfflinePlayer object which is the seller
     * @see OfflinePlayer
     */
    private final OfflinePlayer offlinePlayer;

    /**
     * Is sell type is geyser
     * @see xyz.geik.farmer.modules.geyser.Geyser
     */
    @Getter
    @Setter
    private boolean isGeyser = false;

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
     * @see HandlerList
     */
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Spigot handlers requirement
     * @return handler list
     */
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Spigot handlers requirement
     *      * @return handler list
     * @return HandlerList list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
