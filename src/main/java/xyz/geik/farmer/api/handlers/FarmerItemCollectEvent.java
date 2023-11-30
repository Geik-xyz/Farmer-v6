package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.model.Farmer;

/**
 * FarmerItemCollectEvent fires when Farmer collects an item
 *
 * @author poyrazinan
 */
@Getter
public class FarmerItemCollectEvent extends Event {

    /**
     * Farmer object which collects the item
     * @see Farmer
     */
    private final Farmer farmer;

    /**
     * Item which is collected
     * @see ItemStack
     */
    private final ItemStack item;

    /**
     * Amount of item which is collected
     */
    private final int collectAmount;

    /**
     * Cancel state of event
     */
    private boolean isCancelled = false;

    /**
     * Inherited class
     */
    private final ItemSpawnEvent itemSpawnEvent;

    /**
     * FarmerItemCollectEvent constructor which takes farmer, item, collectAmount and event as parameters
     * <p>Event can be cancelable</p>
     *
     * @param farmer Farmer object which collects the item
     * @param item Item which is collected
     * @param collectAmount Amount of item which is collected
     * @param event Inherited class
     * @see ItemSpawnEvent
     * @see Farmer
     * @see ItemStack
     */
    public FarmerItemCollectEvent(Farmer farmer, ItemStack item, int collectAmount, ItemSpawnEvent event) {
        this.farmer = farmer;
        this.item = item;
        this.collectAmount = collectAmount;
        this.itemSpawnEvent = event;
    }

    /**
     * Cancels the event
     *
     * @param status status of cancel, true for cancel
     */
    public void setCancelled(boolean status) {
        this.isCancelled = status;
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
