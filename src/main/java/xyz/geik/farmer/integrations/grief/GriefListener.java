package xyz.geik.farmer.integrations.grief;

import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.geik.farmer.api.FarmerAPI;

/**
 * GriefPrevention listener class
 * Which is removing farmer if there is
 * a farmer on region.
 */
public class GriefListener implements Listener {

    public GriefListener() {}

    /**
     * Calls ClaimDeleteEvent for removing farmer.
     *
     * @param e
     */
    @EventHandler
    public void claimDeleteEvent(ClaimDeletedEvent e) {
        String regionID = e.getClaim().getID().toString();
        FarmerAPI.removeFarmer(regionID);
    }
}
