package xyz.geik.farmer.integrations.townyadvanced;

import com.palmergames.bukkit.towny.event.DeleteTownEvent;
import com.palmergames.bukkit.towny.event.NewTownEvent;
import com.palmergames.bukkit.towny.event.player.PlayerEntersIntoTownBorderEvent;
import com.palmergames.bukkit.towny.event.town.TownKickEvent;
import com.palmergames.bukkit.towny.event.town.TownLeaveEvent;
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

public class TownyListener implements Listener {
    @EventHandler
    public void disbandEvent(@NotNull DeleteTownEvent e) {
        FarmerAPI.getFarmerManager().removeFarmer(e.getTownUUID().toString());
    }

    @EventHandler
    public void createTownEvent(@NotNull NewTownEvent e) {
        if (Main.getConfigFile().getSettings().isAutoCreateFarmer()) {
            Farmer farmer = new Farmer(e.getTown().getUUID().toString(),0);
            ChatUtils.sendMessage(e.getTown().getMayor().getPlayer(),
                    Main.getLangFile().getMessages().getBoughtFarmer());
        }
    }

    @EventHandler
    public void transferTown(@NotNull TownMayorChangeEvent e) {
        FarmerAPI.getFarmerManager().changeOwner(e.getOldMayor().getUUID(), e.getNewMayor().getUUID(), e.getTown().getUUID().toString());
    }

    @EventHandler
    public void townJoinEvent(@NotNull PlayerEntersIntoTownBorderEvent e) {
        String townID = e.getEnteredTown().getUUID().toString();
        if (!FarmerManager.getFarmers().containsKey(townID))
            return;
        UUID member = e.getPlayer().getUniqueId();
        Farmer farmer = FarmerManager.getFarmers().get(townID);
        if (farmer.getUsers().stream().noneMatch(user -> user.getUuid().equals(member)))
            farmer.addUser(member, Bukkit.getOfflinePlayer(member).getName(), FarmerPerm.COOP);
    }

    @EventHandler
    public void townLeaveEvent(@NotNull TownLeaveEvent e) {
        kickAndLeaveEvent(e.getTown().getUUID().toString(), e.getResident().getPlayer().getUniqueId());
    }

    @EventHandler
    public void townKickEvent(@NotNull TownKickEvent e) {
        kickAndLeaveEvent(e.getTown().getUUID().toString(), e.getKickedResident().getPlayer().getUniqueId());
    }

    private void kickAndLeaveEvent(String townID, UUID member) {
        if (!FarmerManager.getFarmers().containsKey(townID))
            return;
        Farmer farmer = FarmerManager.getFarmers().get(townID);
        if (farmer.getUsers().stream().anyMatch(user -> user.getUuid().equals(member)))
            farmer.removeUser(farmer.getUsers().stream().filter(user -> user.getUuid().equals(member)).findFirst().get());
    }
}
