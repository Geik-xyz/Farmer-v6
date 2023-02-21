package xyz.geik.farmer.integrations.bentobox;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.api.events.island.IslandCreatedEvent;
import world.bentobox.bentobox.api.events.island.IslandDeleteEvent;
import world.bentobox.bentobox.api.events.island.IslandDeletedEvent;
import world.bentobox.bentobox.api.events.team.TeamSetownerEvent;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.model.Farmer;

public class BentoListener implements Listener {

    /**
     * Island delete evet for remove farmer
     * @param e
     */
    @EventHandler
    public void deleteEvent(@NotNull IslandDeleteEvent e) {
        String islandID = e.getIsland().getUniqueId().toString();
        FarmerAPI.removeFarmer(islandID);
    }

    /**
     * Island delete event for remove farmer
     * @param e
     */
    @EventHandler
    public void ownerChangeEvent(@NotNull TeamSetownerEvent e) {
        FarmerAPI.changeOwner(e.getOldOwner(), e.getNewOwner(), e.getIsland().getUniqueId().toString());
    }

    /**
     * Automatically creates farmer
     * when island is created
     *
     * @param e
     */
    @EventHandler
    public void islandCreateEvent(@NotNull IslandCreatedEvent e) {
        new Farmer(e.getIsland().getUniqueId(), e.getOwner());
        Bukkit.getPlayer(e.getOwner()).sendMessage(Main.getLangFile().getText("boughtFarmer"));
    }
}
