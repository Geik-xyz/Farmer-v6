package xyz.geik.farmer.integrations.iridiumskyblock;


import com.iridium.iridiumskyblock.api.IslandDeleteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.geik.farmer.api.FarmerAPI;


public class IridiumListener implements Listener {

    public IridiumListener() {}

    @EventHandler
    public void islandDeleteEvent(IslandDeleteEvent event){
        String regionID = String.valueOf(event.getIsland().getId());
        FarmerAPI.getFarmerManager().removeFarmer(regionID);
    }

}
