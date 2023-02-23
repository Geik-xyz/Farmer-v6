package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.model.Farmer;

@Getter
public class FarmerItemSellEvent extends Event {

    // Farmer object
    private Farmer farmer;
    private Material material;

    // Main constructor of event
    public FarmerItemSellEvent(Farmer farmer, Material material) {
        this.farmer = farmer;
        this.material = material;
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
