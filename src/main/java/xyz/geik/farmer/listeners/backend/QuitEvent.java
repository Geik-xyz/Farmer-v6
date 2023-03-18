package xyz.geik.farmer.listeners.backend;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.Farmer;

import java.util.UUID;

/**
 * Player quit event basically save farmer when player quits
 */
public class QuitEvent implements Listener {

    /**
     * Update database when a player disconnects in a
     * Farmer region. All farmers save on stop only.
     * (No need to be a user of a farmer)
     *
     * @param e
     */
    @EventHandler
    public void onQuitEvent(@NotNull PlayerQuitEvent e) {
        final Location loc = e.getPlayer().getLocation();
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            if (Settings.isWorldAllowed(loc.getWorld().getName())) {
                try {
                    String regionID = Main.getIntegration().getRegionID(loc);
                    if (regionID == null || !FarmerAPI.getFarmerManager().getFarmers().containsKey(regionID))
                        return;
                    Farmer farmer = FarmerAPI.getFarmerManager().getFarmers().get(regionID);
                    farmer.saveFarmerAsync();
                }
                catch (Exception ex) {}
            }
        });
    }
}
