package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import xyz.geik.farmer.model.Farmer;

/**
 * FarmerStorageFullEvent
 */
@Getter
public class FarmerStorageFullEvent extends Event {

    // Farmer object
    private Farmer farmer;
    private ItemStack item;
    private int leftAmount = 0;

    // is cancelled boolean
    private boolean isCancelled = false, dropItem = true;

    private ItemSpawnEvent itemSpawnEvent;

    // Main constructor of event
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

    // Drop item or add to farmer storage
    public void setDropItem(boolean arg0) {
        this.dropItem = arg0;
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
