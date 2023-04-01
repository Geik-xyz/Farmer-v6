package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.model.Farmer;

/**
 * FarmerRemoveEvent is called when a farmer is removed
 *
 * @author poyrazinan
 */
@Getter
public class FarmerRemoveEvent extends Event {

    /**
     * Farmer object of event
     * @see Farmer
     */
    private final Farmer farmer;

    /**
     * Cancelled state of event
     */
    private boolean isCancelled = false;

    /**
     * Constructor of FarmerRemoveEvent class with farmer object as parameter
     * @param farmer Farmer object of event
     *               @see Farmer
     */
    public FarmerRemoveEvent(Farmer farmer) {
        this.farmer = farmer;
    }

    /**
     * Sets cancelled state of event
     * @param status Cancelled state of event (true/false)
     */
    public void setCancelled(boolean status) {
        this.isCancelled = status;
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
