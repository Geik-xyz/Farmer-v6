package xyz.geik.farmer.api.handlers;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import xyz.geik.farmer.model.Farmer;

/**
 * FarmerItemSellEvent
 */
@Getter
public class FarmerItemSellEvent extends Event {

    // Farmer object
    private Farmer farmer;
    private ItemStack itemStack;

    // Main constructor of event
    public FarmerItemSellEvent(Farmer farmer, XMaterial material) {
        this.farmer = farmer;
        this.itemStack = material.parseItem();
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
