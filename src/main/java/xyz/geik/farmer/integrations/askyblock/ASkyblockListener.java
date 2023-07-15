package xyz.geik.farmer.integrations.askyblock;

import com.wasteofplastic.askyblock.events.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.user.FarmerPerm;

import java.util.UUID;

/**
 * ASkyblock integration listeners
 */
public class ASkyblockListener implements Listener {

    /**
     * Constructor register event of super class
     */
    public ASkyblockListener() {}

    /**
     * Remove farmer on island deletion
     * @param e
     */
    @EventHandler
    public void onIslandDeleteEvent(@NotNull IslandDeleteEvent e) {
        FarmerAPI.getFarmerManager().removeFarmer(e.getPlayerUUID().toString());
    }

    /**
     * Remove farmer on island reset
     * @param e
     */
    @EventHandler
    public void onIslandResetEvent(@NotNull IslandResetEvent e) {
        FarmerAPI.getFarmerManager().removeFarmer(e.getPlayer().getUniqueId().toString());
    }

    /**
     * Change farmer owner on island transfer
     * @param e
     */
    @EventHandler
    public void onIslandOwnerChangeEvent(@NotNull IslandChangeOwnerEvent e) {
        FarmerAPI.getFarmerManager().changeOwner(e.getOldOwner(), e.getNewOwner(), e.getOldOwner().toString());
    }

    /**
     * Adds user to farmer
     * @param e
     */
    @EventHandler
    public void onPlayerKickEvent(@NotNull TeamLeaveEvent e) {
        if (!FarmerManager.getFarmers().containsKey(e.getOldTeamLeader().toString()))
            return;
        UUID member = e.getPlayer();
        Farmer farmer = FarmerManager.getFarmers().get(e.getOldTeamLeader().toString());
        // Removes player if added to farmer
        if (farmer.getUsers().stream().anyMatch(user -> user.getUuid().equals(member)))
            farmer.removeUser(farmer.getUsers().stream().filter(user -> user.getUuid().equals(member)).findFirst().get());
    }

    /**
     * Removes user from farmer if added on leave
     * @param e
     */
    @EventHandler
    public void onPlayerJoinEvent(@NotNull TeamJoinEvent e) {
        if (!FarmerManager.getFarmers().containsKey(e.getNewTeamLeader().toString()))
            return;
        UUID member = e.getPlayer();
        Farmer farmer = FarmerManager.getFarmers().get(e.getNewTeamLeader().toString());
        // Adds player if added to farmer
        if (farmer.getUsers().stream().noneMatch(user -> user.getUuid().equals(member)))
            farmer.addUser(member, Bukkit.getOfflinePlayer(member).getName(), FarmerPerm.COOP);
    }
}
