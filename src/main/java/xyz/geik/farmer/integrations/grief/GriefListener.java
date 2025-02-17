package xyz.geik.farmer.integrations.grief;

import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.api.FarmerAPI;

/**
 * GriefPrevention listener class
 * Which is removing farmer if there is
 * a farmer on region.
 */
public class GriefListener implements Listener {

    /**
     * Constructor of class
     */
    public GriefListener() {}

    /**
     * Calls ClaimDeleteEvent for removing farmer.
     *
     * @param e of event
     */
    @EventHandler
    public void claimDeleteEvent(@NotNull ClaimDeletedEvent e) {
        String regionID = e.getClaim().getID().toString();
        FarmerAPI.getFarmerManager().removeFarmer(regionID, e.getClaim().getOwnerID());
    }
}
