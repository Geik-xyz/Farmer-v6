package xyz.geik.farmer.integrations.townyadvanced;

import com.palmergames.bukkit.towny.event.DeleteTownEvent;
import com.palmergames.bukkit.towny.event.NewTownEvent;
import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import com.palmergames.bukkit.towny.event.town.TownMayorChangeEvent;
import org.bukkit.Bukkit;
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
 * TownyAdvanced listener class
 *
 * @author amownyy
 * @since v6-b001
 */
public class TownyListener implements Listener {

    /**
     * Constructor of class
     */
    public TownyListener() {}

    /**
     * Remove farmer on town deletion
     * @param e of event
     */
    @EventHandler
    public void disbandEvent(@NotNull DeleteTownEvent e) {
        FarmerAPI.getFarmerManager().removeFarmer(e.getTownUUID().toString(), e.getMayorUUID());
    }

    /**
     * Automatically creates farmer
     * when town is created
     *
     * @param e of event
     */
    @EventHandler
    public void createTownEvent(@NotNull NewTownEvent e) {
        if (Main.getConfigFile().getSettings().isAutoCreateFarmer()) {
            Farmer farmer = new Farmer(e.getTown().getUUID().toString(),0);
            ChatUtils.sendMessage(e.getTown().getMayor().getPlayer(),
                    Main.getLangFile().getMessages().getBoughtFarmer());
        }
    }

    /**
     * Transfers farmer when town transfer
     *
     * @param e transfer town event
     */
    @EventHandler
    public void transferTown(@NotNull TownMayorChangeEvent e) {
        FarmerAPI.getFarmerManager().changeOwner(e.getOldMayor().getUUID(), e.getNewMayor().getUUID(), e.getTown().getUUID().toString());
    }

    /**
     * Adds user to farmer
     * @param e of event
     */
    @EventHandler
    public void townResidentAddEvent(@NotNull TownAddResidentEvent e) {
        String townID = e.getTown().getUUID().toString();
        if (!FarmerManager.getFarmers().containsKey(townID))
            return;
        UUID member = e.getResident().getUUID();
        Farmer farmer = FarmerManager.getFarmers().get(townID);
        if (farmer.getUsers().stream().noneMatch(user -> user.getUuid().equals(member)))
            farmer.addUser(member, Bukkit.getOfflinePlayer(member).getName(), FarmerPerm.COOP);
    }


    /**
     * Removes user from farmer
     * @param e of event
     */
    @EventHandler
    public void townResidentRemoveEvent(@NotNull TownRemoveResidentEvent e) {
        String townID = e.getTown().getUUID().toString();
        if (!FarmerManager.getFarmers().containsKey(townID))
            return;
        UUID member = e.getResident().getUUID();
        Farmer farmer = FarmerManager.getFarmers().get(townID);
        if (farmer.getUsers().stream().anyMatch(user -> user.getUuid().equals(member)))
            farmer.removeUser(farmer.getUsers().stream().filter(user -> user.getUuid().equals(member)).findFirst().get());

    }

}
