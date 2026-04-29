package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * FarmerLoadedEvent is called when all farmers are loaded from database
 * This event is useful for plugins that depend on Farmer data
 *
 * @author Yarrak
 */
@Getter
public class FarmerLoadedEvent extends Event {

    /**
     * Count of farmers loaded
     */
    private final int farmerCount;

    /**
     * FarmerLoadedEvent constructor
     *
     * @param farmerCount Count of farmers loaded from database
     */
    public FarmerLoadedEvent(int farmerCount) {
        this.farmerCount = farmerCount;
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
     * @return handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}