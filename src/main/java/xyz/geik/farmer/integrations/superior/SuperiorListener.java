package xyz.geik.farmer.integrations.superior;

import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.Farmer;

/**
 * SuperiorSkyblock2 listener class
 * Which is removing farmer if there is
 * a farmer on island.
 */
public class SuperiorListener implements Listener {

    public SuperiorListener() {}

    /**
     * Island delete event for remove farmer
     * @param e
     */
    @EventHandler
    public void disbandEvent(@NotNull IslandDisbandEvent e) {
        FarmerAPI.removeFarmer(e.getIsland().getOwner().getUniqueId().toString());
    }

    /**
     * Automatically creates farmer
     * when island is created
     *
     * @param e
     */
    @EventHandler
    public void createIslandEvent(IslandCreateEvent e) {
        if (Settings.autoCreateFarmer) {
            Farmer farmer = new Farmer(e.getIsland().getOwner().getUniqueId().toString(), e.getIsland().getOwner().getUniqueId(), 0);
            e.getIsland().getOwner().asPlayer().sendMessage(Main.getLangFile().getText("boughtFarmer"));
        }
    }
}
