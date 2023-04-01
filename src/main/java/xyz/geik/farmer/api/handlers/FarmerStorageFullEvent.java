package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import xyz.geik.farmer.model.Farmer;

/**
 * FarmerStorageFullEvent fired when farmer storage is full
 *
 * @author poyrazinan
 */
@Getter
public class FarmerStorageFullEvent extends Event {

    /**
     * Farmer object which storage is full
     * @see Farmer
     */
    private Farmer farmer;

    /**
     * Item which farmer can't add to storage
     * @see ItemStack
     */
    private ItemStack item;

    /**
     * Left amount of item which farmer can't add to storage
     */
    private int leftAmount = 0;

    // Drop item and cancel event booleans
    private boolean isCancelled = false, dropItem = true;

    /**
     * Inherited class
     * @see ItemSpawnEvent
     */
    private ItemSpawnEvent itemSpawnEvent;

    /**
     * FarmerStorageFullEvent constructor with farmer, item, leftAmount and itemSpawnEvent parameters
     * @param farmer Farmer object which storage is full
     * @param item Item which farmer can't add to storage
     * @param leftAmount Left amount of item which farmer can't add to storage
     * @param itemSpawnEvent Inherited class
     *                       @see ItemSpawnEvent
     *                       @see Farmer
     *                       @see ItemStack
     */
    public FarmerStorageFullEvent(Farmer farmer, ItemStack item, int leftAmount, ItemSpawnEvent itemSpawnEvent) {
        this.farmer = farmer;
        this.item = item;
        this.leftAmount = leftAmount;
        this.itemSpawnEvent = itemSpawnEvent;
    }

    // Only setter method of event
    public void setCancelled(boolean arg0) {
        this.isCancelled = arg0;
    }

    /**
     * Whether to drop the item or not
     *
     * @param status true to drop the item, false to not drop the item
     */
    public void setDropItem(boolean status) {
        this.dropItem = status;
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
