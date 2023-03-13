package xyz.geik.farmer.integrations.askyblock;

import com.wasteofplastic.askyblock.events.IslandChangeOwnerEvent;
import com.wasteofplastic.askyblock.events.IslandDeleteEvent;
import com.wasteofplastic.askyblock.events.IslandResetEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.api.FarmerAPI;

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
        FarmerAPI.getFarmerManager().removeFarmer(e.getPlayerUUID().toString());
    }

    /**
     * Remove farmer on island reset
     * @param e
     */
    @EventHandler
    public void onIslandResetEvent(@NotNull IslandResetEvent e) {
        FarmerAPI.getFarmerManager().removeFarmer(e.getPlayer().getUniqueId().toString());
    }

    /**
     * Change farmer owner on island transfer
     * @param e
     */
    @EventHandler
    public void onIslandOwnerChangeEvent(@NotNull IslandChangeOwnerEvent e) {
        FarmerAPI.getFarmerManager().changeOwner(e.getOldOwner(), e.getNewOwner(), e.getOldOwner().toString());
    }
}
