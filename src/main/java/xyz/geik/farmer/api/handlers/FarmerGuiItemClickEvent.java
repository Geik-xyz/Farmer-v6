package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.glib.shades.inventorygui.InventoryGui;

/**
 * FarmerItemSellEvent event fires when a player sells a FarmerItem
 * @see Farmer
 * @see FarmerItem
 * @author poyrazinan
 */
@Getter
public class FarmerGuiItemClickEvent extends Event {

    /**
     * Farmer object which contains the FarmerItem
     * @see Farmer
     */
    private final Farmer farmer;

    /**
     * FarmerItem object which is sold
     * @see FarmerItem
     */
    private final FarmerItem farmerItem;

    /**
     * OfflinePlayer object which is the seller
     * @see OfflinePlayer
     */
    private final OfflinePlayer offlinePlayer;

    /**
     * GuiClick Type
     */
    private final ClickType guiClickEvent;

    /**
     * Gui object
     */
    private final InventoryGui gui;

    /**
     * FarmerItemSellEvent constructor with Farmer, FarmerItem and OfflinePlayer
     *
     * @param farmer Farmer object which contains the FarmerItem
     * @param farmerItem FarmerItem object which is sold
     * @param offlinePlayer OfflinePlayer object which is the seller
     */
    public FarmerGuiItemClickEvent(Farmer farmer, FarmerItem farmerItem, OfflinePlayer offlinePlayer, ClickType type, InventoryGui gui) {
        this.farmer = farmer;
        this.farmerItem = farmerItem;
        this.offlinePlayer = offlinePlayer;
        this.guiClickEvent = type;
        this.gui = gui;
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
