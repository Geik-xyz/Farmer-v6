package xyz.geik.farmer.integrations.ultimateclaims;

import com.craftaro.ultimateclaims.api.events.*;
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

public class UltimateListener implements Listener {

    public UltimateListener(){}

    @EventHandler
    public void claimCreateEvent(ClaimCreateEvent event){
        String claimId = String.valueOf(event.getClaim().getId());
        if (Main.getConfigFile().getSettings().isAutoCreateFarmer()) {
            Farmer farmer = new Farmer(claimId, 0);
            ChatUtils.sendMessage(event.getClaim().getOwner().getPlayer().getPlayer(),
                    Main.getLangFile().getMessages().getBoughtFarmer());
        }
    }

    @EventHandler
    public void claimJoinEvent(ClaimMemberAddEvent event){
        String claimId = String.valueOf(event.getClaim().getId());
        if (!FarmerManager.getFarmers().containsKey(claimId)) return;
        UUID member = event.getPlayer().getUniqueId();
        Farmer farmer = FarmerManager.getFarmers().get(claimId);
        if (farmer.getUsers().stream().noneMatch(user -> user.getUuid().equals(member)))
            farmer.addUser(member, Bukkit.getOfflinePlayer(member).getName(), FarmerPerm.COOP);
    }

    @EventHandler
    public void claimLeaveEvent(ClaimMemberLeaveEvent event){
        kickAndLeaveEvent(String.valueOf(event.getClaim().getId()),event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void claimBanEvent(ClaimPlayerBanEvent event){
        kickAndLeaveEvent(String.valueOf(event.getClaim().getId()),event.getBannedPlayer().getUniqueId());

    }

    @EventHandler
    public void claimTransferEvent(ClaimTransferOwnershipEvent event){
        FarmerAPI.getFarmerManager()
                .changeOwner(event.getOldOwner().getUniqueId(), event.getNewOwner().getUniqueId(), String.valueOf(event.getClaim().getId()));
    }

    @EventHandler
    public void claimDeleteEvent(ClaimDeleteEvent event){
        FarmerAPI.getFarmerManager().removeFarmer(String.valueOf(event.getClaim().getId()));
    }



    private void kickAndLeaveEvent(String claimId, UUID member) {
        if (!FarmerManager.getFarmers().containsKey(claimId))
            return;
        Farmer farmer = FarmerManager.getFarmers().get(claimId);
        // Removes player if added to farmer
        if (farmer.getUsers().stream().anyMatch(user -> user.getUuid().equals(member)))
            farmer.removeUser(farmer.getUsers().stream().filter(user -> user.getUuid().equals(member)).findFirst().get());
    }


}
