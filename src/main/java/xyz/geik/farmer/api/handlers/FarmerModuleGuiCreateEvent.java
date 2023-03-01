package xyz.geik.farmer.api.handlers;

import de.themoep.inventorygui.InventoryGui;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.model.Farmer;

@Getter
public class FarmerModuleGuiCreateEvent extends Event {

    private Player player;
    private InventoryGui gui;

    private Farmer farmer;

    private boolean isCancelled = false;

    // Main constructor of event
    public FarmerModuleGuiCreateEvent(Player player, InventoryGui gui, Farmer farmer) {
        this.player = player;
        this.gui = gui;
        this.farmer = farmer;
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