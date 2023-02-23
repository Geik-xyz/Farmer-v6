package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.model.Farmer;

@Getter
public class FarmerStorageFullEvent extends Event {

    // Farmer object
    private Farmer farmer;
    private Material material;

    // is cancelled boolean
    private boolean isCancelled = false, dropItem = true;

    // Main constructor of event
    public FarmerStorageFullEvent(Farmer farmer, Material material) {
        this.farmer = farmer;
        this.material = material;
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
