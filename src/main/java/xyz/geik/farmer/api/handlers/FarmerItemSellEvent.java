package xyz.geik.farmer.api.handlers;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerItem;

/**
 * FarmerItemSellEvent
 */
@Getter
public class FarmerItemSellEvent extends Event {

    // Farmer object
    private Farmer farmer;
    private FarmerItem farmerItem;

    private final OfflinePlayer offlinePlayer;

    // Main constructor of event
    public FarmerItemSellEvent(Farmer farmer, FarmerItem farmerItem, OfflinePlayer offlinePlayer) {
        this.farmer = farmer;
        this.farmerItem = farmerItem;
        this.offlinePlayer = offlinePlayer;
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
