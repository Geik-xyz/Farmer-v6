package xyz.geik.farmer.integrations.fabledskyblock;

import com.songoda.skyblock.api.event.island.IslandCreateEvent;
import com.songoda.skyblock.api.event.island.IslandDeleteEvent;
import com.songoda.skyblock.api.event.island.IslandLevelChangeEvent;
import com.songoda.skyblock.api.event.island.IslandOwnershipTransferEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.Farmer;

/**
 * @author mehmet-27
 */
public class FabledSkyListener implements Listener {

    /**
     * Automatically creates a farmer when island is created
     * @param event listener event of create
     */
    @EventHandler
    public void onIslandCreate(IslandCreateEvent event) {
        if (Settings.autoCreateFarmer) {
            new Farmer(event.getIsland().getIslandUUID().toString(), event.getIsland().getOwnerUUID(), 0);
            Player player = Bukkit.getPlayer(event.getIsland().getOwnerUUID());
            if (player != null) player.sendMessage(Main.getLangFile().getText("boughtFarmer"));
        }
    }

    /**
     * Remove farmer on island delete
     * @param event listener event of delete
     */
    @EventHandler
    public void onIslandDelete(IslandDeleteEvent event) {
        FarmerAPI.getFarmerManager().removeFarmer(event.getIsland().getIslandUUID().toString());
    }

    /**
     * Change farmer owner on island transfer
     * @param event listener event of transfer
     */
    @EventHandler
    public void transferIslandEvent(IslandOwnershipTransferEvent event) {
        FarmerAPI.getFarmerManager().changeOwner(event.getIsland().getOriginalOwnerUUID(),
                event.getOwner().getUniqueId(),
                event.getIsland().getIslandUUID().toString());

    }

    /**
     * Remove farmer on island reset
     * @param event listener event of reset
     */
    @EventHandler
    public void onIslandResetEvent(@NotNull IslandLevelChangeEvent event) {
        if (event.getLevel().getLevel() == 0) {
            FarmerAPI.getFarmerManager().removeFarmer(event.getIsland().getIslandUUID().toString());
        }
    }
}
