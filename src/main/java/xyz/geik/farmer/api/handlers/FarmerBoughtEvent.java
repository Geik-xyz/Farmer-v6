package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
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
    private final Farmer farmer;

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
     * @see HandlerList
     */
    private static final HandlerList handlers = new HandlerList();

    /**
     * Spigot handlers requirement
     * @return handler list
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
