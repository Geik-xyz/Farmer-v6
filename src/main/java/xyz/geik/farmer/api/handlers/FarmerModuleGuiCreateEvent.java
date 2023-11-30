package xyz.geik.farmer.api.handlers;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.glib.shades.inventorygui.InventoryGui;

/**
 * FarmerModuleGuiCreateEvent is called when a farmer module gui is created
 *
 * @author poyrazinan
 */
@Getter
public class FarmerModuleGuiCreateEvent extends Event {

    /**
     * Player who opened the gui
     * @see Player
     */
    private final Player player;

    /**
     * Gui that has created
     * @see InventoryGui
     */
    private final InventoryGui gui;

    /**
     * Farmer that has opened the gui as reference
     * @see Farmer
     */
    private final Farmer farmer;

    /**
     * Cancel status of event (true = cancelled)
     */
    private boolean isCancelled = false;

    /**
     * FarmerModuleGuiCreateEvent constructor
     *
     * @param player who opened the gui
     * @param gui that has created
     * @param farmer that has opened the gui as reference
     * @see Player
     * @see InventoryGui
     * @see Farmer
     */
    public FarmerModuleGuiCreateEvent(Player player, InventoryGui gui, Farmer farmer) {
        this.player = player;
        this.gui = gui;
        this.farmer = farmer;
    }

    /**
     * Cancel event
     *
     * @param isCancelled status of event (true = cancelled)
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