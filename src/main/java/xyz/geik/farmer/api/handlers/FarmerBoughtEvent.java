package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.model.Farmer;

/**
 * FarmerBoughtEvent fired when a farmer is bought
 *
 * @author poyrazinan
 */
@Getter
public class FarmerBoughtEvent extends Event {

    /**
     * Bought farmer object
     * @see Farmer
     */
    private Farmer farmer;

    /**
     * FarmerBoughtEvent constructor
     *
     * @param farmer Bought farmer object
     *               @see Farmer
     */
    public FarmerBoughtEvent(Farmer farmer) {
        this.farmer = farmer;
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

    /**
     * @return HandlerList
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
