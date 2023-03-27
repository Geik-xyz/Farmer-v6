package xyz.geik.farmer.integrations.bentobox;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.api.events.island.IslandCreatedEvent;
import world.bentobox.bentobox.api.events.island.IslandDeleteEvent;
import world.bentobox.bentobox.api.events.island.IslandResetEvent;
import world.bentobox.bentobox.api.events.team.TeamSetownerEvent;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.Farmer;

import java.util.UUID;

public class BentoListener implements Listener {

    /**
     * Island delete evet for remove farmer
     * @param e
     */
    @EventHandler
    public void deleteEvent(@NotNull IslandDeleteEvent e) {
        String islandID = e.getIsland().getUniqueId().toString();
        FarmerAPI.getFarmerManager().removeFarmer(islandID);
    }

    /**
     * Island reset event for remove farmer
     *
     * @param e
     */
    @EventHandler
    public void resetEvent(IslandResetEvent e) {
        String islandID = e.getOldIsland().getUniqueId().toString();
        FarmerAPI.getFarmerManager().removeFarmer(islandID);
        autoCreate(e.getIsland().getUniqueId(), e.getOwner());
    }

    /**
     * Island delete event for remove farmer
     * @param e
     */
    @EventHandler
    public void ownerChangeEvent(@NotNull TeamSetownerEvent e) {
        FarmerAPI.getFarmerManager().changeOwner(e.getOldOwner(), e.getNewOwner(), e.getIsland().getUniqueId().toString());
    }

    /**
     * Automatically creates farmer
     * when island is created
     *
     * @param e
     */
    @EventHandler
    public void islandCreateEvent(@NotNull IslandCreatedEvent e) {
        autoCreate(e.getIsland().getUniqueId(), e.getOwner());
    }

    private void autoCreate(String regionId, UUID owner) {
        if (Settings.autoCreateFarmer) {
            new Farmer(regionId, owner, 0);
            Bukkit.getPlayer(owner).sendMessage(Main.getLangFile().getText("boughtFarmer"));
        }
    }
}
