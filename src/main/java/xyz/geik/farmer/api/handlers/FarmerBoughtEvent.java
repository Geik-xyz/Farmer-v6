package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.model.Farmer;

/**
 * FarmerBoughtEvent
 */
@Getter
public class FarmerBoughtEvent extends Event {

    // Farmer object
    private Farmer farmer;

    // Main constructor of event
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

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
