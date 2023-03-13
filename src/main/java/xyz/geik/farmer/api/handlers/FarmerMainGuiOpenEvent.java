package xyz.geik.farmer.api.handlers;

import de.themoep.inventorygui.InventoryGui;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.model.Farmer;

@Getter
public class FarmerMainGuiOpenEvent extends Event {

    private Player player;

    // Farmer object
    private Farmer farmer;
    private InventoryGui gui;

    private boolean isCancelled = false;

    // Main constructor of event
    public FarmerMainGuiOpenEvent(Player player, Farmer farmer, InventoryGui gui) {
        this.farmer = farmer;
        this.player = player;
        this.gui = gui;
    }

    /**
     * Cancel event
     *
     * @param isCancelled
     */
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
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