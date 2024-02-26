package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.glib.shades.inventorygui.InventoryGui;

/**
 * FarmerMainGuiOpenEvent fired when a player open the main gui
 *
 * @author poyrazinan
 */
@Getter
public class FarmerMainGuiOpenEvent extends Event {

    /**
     * Player object of event
     * @see Player
     */
    private final Player player;

    /**
     * Farmer object of event
     * @see Farmer
     */
    private final Farmer farmer;

    /**
     * InventoryGui object of event
     * @see InventoryGui
     * @see InventoryGui#show(org.bukkit.entity.HumanEntity)
     */
    private final InventoryGui gui;

    /**
     * Event status (cancelled or not)
     */
    private boolean isCancelled = false;

    /**
     * FarmerMainGuiOpenEvent constructor with player, farmer and gui objects as parameters
     *
     * @param player Player who open the gui
     * @param farmer Farmer object of player
     * @param gui InventoryGui object of farmer
     * @see InventoryGui
     * @see InventoryGui#show(org.bukkit.entity.HumanEntity)
     * @see Player
     * @see Farmer
     */
    public FarmerMainGuiOpenEvent(Player player, Farmer farmer, InventoryGui gui) {
        this.farmer = farmer;
        this.player = player;
        this.gui = gui;
    }

    /**
     * Cancel event
     *
     * @param isCancelled Event status
     */
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
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