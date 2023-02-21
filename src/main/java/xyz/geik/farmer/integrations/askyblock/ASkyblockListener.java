package xyz.geik.farmer.integrations.askyblock;

import com.wasteofplastic.askyblock.events.IslandChangeOwnerEvent;
import com.wasteofplastic.askyblock.events.IslandDeleteEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.api.events.island.IslandCreatedEvent;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.Farmer;

/**
 * ASkyblock integration listeners
 */
public class ASkyblockListener implements Listener {

    /**
     * Constructor register event of super class
     */
    public ASkyblockListener() {}

    /**
     * Remove farmer on island deletion
     * @param e
     */
    @EventHandler
    public void onIslandDeleteEvent(@NotNull IslandDeleteEvent e) {
        FarmerAPI.removeFarmer(e.getPlayerUUID().toString());
    }

    /**
     * Change farmer owner on island transfer
     * @param e
     */
    @EventHandler
    public void onIslandOwnerChangeEvent(@NotNull IslandChangeOwnerEvent e) {
        FarmerAPI.changeOwner(e.getOldOwner(), e.getNewOwner(), e.getOldOwner().toString());

    }

    /**
     * Create farmer on island creation
     * @param e
     */
    @EventHandler
    public void onIslandCrateEvent(IslandCreatedEvent e) {
        if (Settings.autoCreateFarmer) {
            new Farmer(e.getIsland().getUniqueId(), e.getOwner());
            Bukkit.getPlayer(e.getOwner()).sendMessage(Main.getLangFile().getText("boughtFarmer"));
        }
    }
}
