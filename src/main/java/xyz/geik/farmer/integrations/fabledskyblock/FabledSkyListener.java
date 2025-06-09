package xyz.geik.farmer.integrations.fabledskyblock;

import com.songoda.skyblock.api.event.island.*;
import com.songoda.skyblock.api.event.player.PlayerIslandJoinEvent;
import com.songoda.skyblock.api.event.player.PlayerIslandLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.glib.chat.ChatUtils;

import java.util.UUID;

/**
 * Fabledskyblock integration listener class
 *
 * @author mehmet-27
 */
public class FabledSkyListener implements Listener {

    /**
     * Constructor of class
     */
    public FabledSkyListener() {}

    /**
     * Automatically creates a farmer when island is created
     * @param event listener event of create
     */
    @EventHandler
    public void onIslandCreate(IslandCreateEvent event) {
        if (Main.getConfigFile().getSettings().isAutoCreateFarmer()) {
            new Farmer(event.getIsland().getIslandUUID().toString(), 0);
            Player player = Bukkit.getPlayer(event.getIsland().getOwnerUUID());
            if (player != null)
                ChatUtils.sendMessage(player,
                        Main.getLangFile().getMessages().getBoughtFarmer());
        }
    }

    /**
     * Remove farmer on island delete
     * @param event listener event of delete
     */
    @EventHandler
    public void onIslandDelete(@NotNull IslandDeleteEvent event) {
        FarmerAPI.getFarmerManager().removeFarmer(event.getIsland().getIslandUUID().toString(), event.getIsland().getOwnerUUID());
    }

    /**
     * Change farmer owner on island transfer
     * @param event listener event of transfer
     */
    @EventHandler
    public void transferIslandEvent(@NotNull IslandOwnershipTransferEvent event) {
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
            FarmerAPI.getFarmerManager().removeFarmer(event.getIsland().getIslandUUID().toString(), event.getIsland().getOwnerUUID());
        }
    }

    /**
     * Adds user to farmer
     * @param e of event
     */
    @EventHandler
    public void islandJoinEvent(@NotNull PlayerIslandJoinEvent e) {
        String islandId = e.getIsland().getIslandUUID().toString();
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
     * @param e of event
     */
    @EventHandler
    public void teamLeaveEvent(@NotNull IslandKickEvent e) {
        kickAndLeaveEvent(e.getIsland().getIslandUUID().toString(), e.getKicked().getUniqueId());
    }

    /**
     * Removes user from farmer if added on kick
     * @param e of event
     */
    @EventHandler
    public void teamKickEvent(@NotNull PlayerIslandLeaveEvent e) {
        kickAndLeaveEvent(e.getIsland().getIslandUUID().toString(), e.getPlayer().getUniqueId());
    }

    /**
     * Remove function of kick and leave event
     *
     * @param islandId id of island
     * @param member member of island
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
