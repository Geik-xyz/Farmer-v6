package xyz.geik.farmer.integrations.superior;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.api.FarmerAPI;

/**
 * SuperiorSkyblock2 listener class
 * Which is removing farmer if there is
 * a farmer on island.
 */
public class SuperiorListener implements Listener {


    public SuperiorListener() {}

    @EventHandler
    public void disbandEvent(@NotNull IslandDisbandEvent e) {
        FarmerAPI.removeFarmer(e.getIsland().getOwner().getUniqueId().toString());
    }

    // TODO update owner event
}
