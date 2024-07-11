package xyz.geik.farmer.integrations.lands;

import me.angeschossen.lands.api.events.*;
import me.angeschossen.lands.api.land.Land;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.glib.chat.ChatUtils;

import java.util.UUID;

/**
 * Lands listener class
 *
 * @author amownyy
 * @since v6-b003
 */
public class LandsListener implements Listener {

    /**
     * Constructor of class
     */
    public LandsListener() {}

    /**
     * Remove farmer on land deletion
     * @param event of event
     */
    @EventHandler
    public void removeLandEvent(LandDeleteEvent event) {
        FarmerAPI.getFarmerManager().removeFarmer(event.getLand().getULID().toString());
    }

    /**
     * Automatically creates farmer
     * when land is created
     *
     * @param event of event
     */
    @EventHandler
    public void createLandEvent(LandCreateEvent event) {
        if (Main.getConfigFile().getSettings().isAutoCreateFarmer()) {
            Farmer farmer = new Farmer(event.getLand().getULID().toString(), 0);
            event.getLand().getOnlinePlayers().forEach(player -> ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getBoughtFarmer()));
        }
    }

    /**
     * Transfers farmer when land transfer
     *
     * @param event transfer land event
     */
    @EventHandler
    public void transferLandEvent(LandOwnerChangeEvent event) {
        FarmerAPI.getFarmerManager()
                .changeOwner(event.getPlayerUUID(), event.getTargetUUID(), event.getLand().getULID().toString());
    }

    /**
     * Adds user to farmer
     * @param event of event
     */
    @EventHandler
    public void landJoinEvent(LandTrustPlayerEvent event) {
        Land land = event.getLand();
        String landID = land.getULID().toString();
        if (!FarmerManager.getFarmers().containsKey(landID))
            return;
        UUID member = event.getTargetUUID();
        Farmer farmer = FarmerManager.getFarmers().get(landID);
        // Adds player if added to farmer
        if (farmer.getUsers().stream().noneMatch(user -> user.getUuid().equals(member)))
            farmer.addUser(member, Bukkit.getOfflinePlayer(member).getName(), FarmerPerm.COOP);
    }

    /**
     * Removes user from farmer if added when leave
     * @param event of event
     */
    public void landLeaveEvent(PlayerLeaveLandEvent event) {
        kickAndLeaveEvent(event.getLand().getULID().toString(), event.getPlayerUID());
    }

    /**
     * Removes user from farmer if added when untrust
     * @param event of event
     */
    @EventHandler
    public void landKickEvent(LandUntrustPlayerEvent event) {
        kickAndLeaveEvent(event.getLand().getULID().toString(), event.getTargetUUID());
    }

    /**
     * Remove function of kick and leave event
     *
     * @param landID id of land
     * @param member member of land
     */
    private void kickAndLeaveEvent(String landID, UUID member) {
        if (!FarmerManager.getFarmers().containsKey(landID))
            return;
        Farmer farmer = FarmerManager.getFarmers().get(landID);
        // Removes player if added to farmer
        if (farmer.getUsers().stream().anyMatch(user -> user.getUuid().equals(member)))
            farmer.removeUser(farmer.getUsers().stream().filter(user -> user.getUuid().equals(member)).findFirst().get());
    }
}
