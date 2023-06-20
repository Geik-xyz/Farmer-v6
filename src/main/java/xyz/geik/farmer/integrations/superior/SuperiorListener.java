package xyz.geik.farmer.integrations.superior;

import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandTransferEvent;
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

    /**
     * Superior listener constructor
     */
    public SuperiorListener() {}

    /**
     * Island delete event for remove farmer
     * @param e
     */
    @EventHandler
    public void disbandEvent(@NotNull IslandDisbandEvent e) {
        FarmerAPI.getFarmerManager().removeFarmer(e.getIsland().getUniqueId().toString());
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
            Farmer farmer = new Farmer(e.getIsland().getUniqueId().toString(), e.getIsland().getOwner().getUniqueId(), 0);
            e.getIsland().getOwner().asPlayer().sendMessage(Main.getLangFile().getText("boughtFarmer"));
        }
    }

    /**
     * @param event transfer island event
     */
    @EventHandler
    public void transferIslandEvent(IslandTransferEvent event) {
        FarmerAPI.getFarmerManager()
                .changeOwner(event.getOldOwner().getUniqueId(), event.getNewOwner().getUniqueId(), event.getIsland().getUniqueId().toString());
    }
}
