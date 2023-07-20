package xyz.geik.farmer.integrations.superior;

import com.bgsoftware.superiorskyblock.api.events.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.user.FarmerPerm;

import java.util.UUID;

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
            Farmer farmer = new Farmer(e.getIsland().getUniqueId().toString(), 0);
            e.getIsland().getOwner().asPlayer().sendMessage(Main.getLangFile().getText("boughtFarmer"));
        }
    }

    /**
     * Transfers farmer when island transfer
     *
     * @param event transfer island event
     */
    @EventHandler
    public void transferIslandEvent(IslandTransferEvent event) {
        FarmerAPI.getFarmerManager()
                .changeOwner(event.getOldOwner().getUniqueId(), event.getNewOwner().getUniqueId(), event.getIsland().getUniqueId().toString());
    }

    /**
     * Adds user to farmer
     * @param e
     */
    @EventHandler
    public void islandJoinEvent(@NotNull IslandJoinEvent e) {
        String islandId = e.getIsland().getUniqueId().toString();
        if (!FarmerManager.getFarmers().containsKey(islandId))
            return;
        UUID member = e.getPlayer().getUniqueId();
        Farmer farmer = FarmerManager.getFarmers().get(islandId);
        // Adds player if added to farmer
        if (farmer.getUsers().stream().noneMatch(user -> user.getUuid().equals(member)))
            farmer.addUser(member, Bukkit.getOfflinePlayer(member).getName(), FarmerPerm.COOP);
    }

    /**
     * Removes user from farmer if added on leave
     * @param e
     */
    @EventHandler
    public void teamLeaveEvent(@NotNull IslandKickEvent e) {
        kickAndLeaveEvent(e.getIsland().getUniqueId().toString(), e.getPlayer().getUniqueId());
    }

    /**
     * Removes user from farmer if added on kick
     * @param e
     */
    @EventHandler
    public void teamKickEvent(@NotNull IslandQuitEvent e) {
        kickAndLeaveEvent(e.getIsland().getUniqueId().toString(), e.getPlayer().getUniqueId());
    }

    /**
     * Remove function of kick and leave event
     *
     * @param islandId
     * @param member
     */
    private void kickAndLeaveEvent(String islandId, UUID member) {
        if (!FarmerManager.getFarmers().containsKey(islandId))
            return;
        Farmer farmer = FarmerManager.getFarmers().get(islandId);
        // Removes player if added to farmer
        if (farmer.getUsers().stream().anyMatch(user -> user.getUuid().equals(member)))
            farmer.removeUser(farmer.getUsers().stream().filter(user -> user.getUuid().equals(member)).findFirst().get());
    }
}
