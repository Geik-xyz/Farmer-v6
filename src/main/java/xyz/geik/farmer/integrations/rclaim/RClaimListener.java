package xyz.geik.farmer.integrations.rclaim;

import net.weesli.rClaim.api.RClaimAPI;
import net.weesli.rClaim.api.events.ClaimCreateEvent;
import net.weesli.rClaim.api.events.ClaimDeleteEvent;
import net.weesli.rClaim.api.events.TrustedPlayerEvent;
import net.weesli.rClaim.api.events.UnTrustedPlayerEvent;
import net.weesli.rClaim.utils.Claim;
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

public class RClaimListener implements Listener {


    @EventHandler
    public void createClaim(ClaimCreateEvent e){
        String claimId = e.getClaim().getID();
        if (Main.getConfigFile().getSettings().isAutoCreateFarmer()) {
            Farmer farmer = new Farmer(claimId, 0);
            ChatUtils.sendMessage(e.getSender(), Main.getLangFile().getMessages().getBoughtFarmer());
        }
    }

    @EventHandler
    public void trustPlayer(TrustedPlayerEvent e){
        Claim claim = RClaimAPI.getInstance().getClaims().stream().filter(c -> c.isOwner(e.getTruster().getUniqueId())).toList().get(0);
        if (!FarmerManager.getFarmers().containsKey(claim.getID())) return;
        UUID target = e.getTrusted().getUniqueId();
        Farmer farmer = FarmerManager.getFarmers().get(claim.getID());
        farmer.addUser(target,Bukkit.getOfflinePlayer(target).getName(), FarmerPerm.COOP);
    }

    @EventHandler
    public void untrustPlayer(UnTrustedPlayerEvent e){
        Claim claim = RClaimAPI.getInstance().getClaims().stream().filter(c -> c.isOwner(e.getTruster().getUniqueId())).toList().get(0);
        if (!FarmerManager.getFarmers().containsKey(claim.getID())) return;
        Farmer farmer = FarmerManager.getFarmers().get(claim.getID());
        farmer.removeUser(farmer.getUsersWithoutOwner().stream().filter(u -> u.getUuid().equals(e.getTrusted().getUniqueId())).findFirst().get());
    }


    @EventHandler
    public void deleteClaim(ClaimDeleteEvent e){
        if (FarmerManager.farmers.containsKey(e.getClaim().getID())){
            FarmerAPI.getFarmerManager().removeFarmer(e.getClaim().getID());
        }
    }
}
