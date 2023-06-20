package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.model.Farmer;

/**
 * FarmerItemProductionEvent Event fires when a farmer calculates the production of an item
 * @see xyz.geik.farmer.modules.production.Production
 * @author poyrazinan
 */
@Getter
public class FarmerItemProductionEvent extends Event {

    /**
     * Farmer object of event
     * @see Farmer
     */
    private Farmer farmer;

    /**
     * FarmerItemProductionEvent constructor
     *
     * @param farmer Farmer object which is calculated the production of an item
     */
    public FarmerItemProductionEvent(Farmer farmer) {
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