package xyz.geik.farmer.integrations.fabledskyblock;

import com.songoda.skyblock.api.event.island.IslandDeleteEvent;
import com.songoda.skyblock.api.event.island.IslandOwnershipTransferEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.geik.farmer.api.FarmerAPI;

import java.util.UUID;

public class FabledListener implements Listener {

    /**
     * Constructor register event of super class
     */
    public FabledListener() {}

    /**
     * Remove farmer on island deletion
     * @param event
     */
    @EventHandler
    void onIslandDeleteEvent(IslandDeleteEvent event) {
        FarmerAPI.getFarmerManager().removeFarmer(event.getIsland().getOwnerUUID().toString());
    }


    // IN FABLEDSKYBLOCK THERE IS NO RESET COMMAND, FOR THIS REASON THERE IS NO EVENT FOR THAT


    /**
     * Change farmer owner on island transfer
     * @param event
     */
    @EventHandler
    void onIslandOwnerChangeEvent(IslandOwnershipTransferEvent event) {
        UUID oldOwner = event.getIsland().getOriginalOwnerUUID();
        FarmerAPI.getFarmerManager().changeOwner(oldOwner, event.getOwner().getUniqueId(), oldOwner.toString());
    }

}
