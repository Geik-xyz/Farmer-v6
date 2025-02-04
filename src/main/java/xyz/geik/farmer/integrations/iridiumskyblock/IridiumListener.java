package xyz.geik.farmer.integrations.iridiumskyblock;


import com.iridium.iridiumskyblock.api.IslandDeleteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.geik.farmer.api.FarmerAPI;

import java.util.UUID;

/**
 * IridiumSkyblock listener class
 *
 * @author Khontrom
 */
public class IridiumListener implements Listener {

    /**
     * Constructor of class
     */
    public IridiumListener() {}

    /**
     * Remove farmer on island delete
     * @param event listener event of delete
     */
    @EventHandler
    public void islandDeleteEvent(IslandDeleteEvent event){
        String regionID = String.valueOf(event.getIsland().getId());
        FarmerAPI.getFarmerManager().removeFarmer(regionID, event.getIsland().getOwner().orElseGet(null).getUuid());
    }

}
