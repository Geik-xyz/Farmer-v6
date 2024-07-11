package xyz.geik.farmer.integrations.bentobox;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.api.events.island.IslandCreatedEvent;
import world.bentobox.bentobox.api.events.island.IslandDeleteEvent;
import world.bentobox.bentobox.api.events.island.IslandResettedEvent;
import world.bentobox.bentobox.api.events.team.TeamJoinEvent;
import world.bentobox.bentobox.api.events.team.TeamKickEvent;
import world.bentobox.bentobox.api.events.team.TeamLeaveEvent;
import world.bentobox.bentobox.api.events.team.TeamSetownerEvent;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.farmer.model.user.User;
import xyz.geik.glib.chat.ChatUtils;

import java.util.Optional;
import java.util.UUID;

/**
 * Bento listener class
 */
public class BentoListener implements Listener {

    /**
     * Constructor of class
     */
    public BentoListener() {}

    /**
     * Island delete evet for remove farmer
     * @param e of event
     */
    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void deleteEvent(@NotNull IslandDeleteEvent e) {
        String islandID = e.getIsland().getUniqueId();
        FarmerAPI.getFarmerManager().removeFarmer(islandID);
    }

    /**
     * Island reset event for remove farmer
     *
     * @param e of event
     */
    @EventHandler
    public void resetEvent(@NotNull IslandResettedEvent e) {
        String islandID = e.getOldIsland().getUniqueId();
        FarmerAPI.getFarmerManager().removeFarmer(islandID);
        autoCreate(e.getIsland().getUniqueId(), e.getOwner());
    }

    /**
     * Island delete event for remove farmer
     * @param e of event
     */
    @EventHandler
    public void ownerChangeEvent(@NotNull TeamSetownerEvent e) {
        FarmerAPI.getFarmerManager().changeOwner(e.getOldOwner(), e.getNewOwner(), e.getIsland().getUniqueId());
    }

    /**
     * Adds user to farmer
     * @param e of event
     */
    @EventHandler
    public void teamJoinEvent(@NotNull TeamJoinEvent e) {
        String islandId = e.getIsland().getUniqueId();
        if (!FarmerManager.getFarmers().containsKey(islandId))
            return;
        UUID member = e.getPlayerUUID();
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
    public void teamLeaveEvent(@NotNull TeamLeaveEvent e) {
        kickAndLeaveEvent(e.getIsland().getUniqueId(), e.getPlayerUUID());
    }

    /**
     * Removes user from farmer if added on kick
     * @param e of event
     */
    @EventHandler
    public void teamKickEvent(@NotNull TeamKickEvent e) {
        kickAndLeaveEvent(e.getIsland().getUniqueId(), e.getPlayerUUID());
    }

    /**
     * Remove function of kick and leave event
     *
     * @param islandId id of island
     * @param member member uuid of island
     */
    private void kickAndLeaveEvent(String islandId, UUID member) {
        if (!FarmerManager.getFarmers().containsKey(islandId)) {
            return;
        }

        Farmer farmer = FarmerManager.getFarmers().get(islandId);

        // Check if the farmer contains the user with the given UUID
        Optional<User> userToRemove = farmer.getUsers().stream()
                .filter(user -> user.getUuid().equals(member))
                .findFirst();

        // If the user is present, remove them
        userToRemove.ifPresent(farmer::removeUser);
    }

    /**
     * Automatically creates farmer
     * when island is created
     *
     * @param e of event
     */
    @EventHandler
    public void islandCreateEvent(@NotNull IslandCreatedEvent e) {
        autoCreate(e.getIsland().getUniqueId(), e.getOwner());
    }

    /**
     * Creates farmer automatically function
     *
     * @param regionId id of region
     * @param owner owner uuid
     */
    private void autoCreate(String regionId, UUID owner) {
        if (Main.getConfigFile().getSettings().isAutoCreateFarmer()) {
            new Farmer(regionId, 0);
            Player player = Bukkit.getPlayer(owner);
            if (player != null) {
                ChatUtils.sendMessage(player,
                        Main.getLangFile().getMessages().getBoughtFarmer());
            }
        }
    }
}
