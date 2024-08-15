package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerItem;

/**
 * FarmerItemLoadingEvent fired when a farmer loads item
 *
 * @author poyrazinan
 */
@Getter
public class FarmerPreLoadItemEvent extends Event {

    /**
     * Bought farmer object
     * @see Farmer
     */
    private final FarmerItem farmerItem;

    /**
     * FarmerItemLoadingEvent constructor
     *
     * @param farmerItem item of farmer
     *               @see Farmer
     */
    public FarmerPreLoadItemEvent(FarmerItem farmerItem) {
        this.farmerItem = farmerItem;
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