package xyz.geik.farmer.integrations.rclaim;

import net.weesli.rclaim.api.RClaimProvider;
import net.weesli.rclaim.api.events.ClaimCreateEvent;
import net.weesli.rclaim.api.events.ClaimDeleteEvent;
import net.weesli.rclaim.api.events.ClaimTrustEvent;
import net.weesli.rclaim.api.events.ClaimUnTrustEvent;
import net.weesli.rclaim.api.model.Claim;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.handlers.FarmerBoughtEvent;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.farmer.model.user.User;
import xyz.geik.glib.chat.ChatUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * RClaim Integration Listener class
 *
 * @author Weesli
 * @since 25.08.2024
 */
public class RClaimListener implements Listener {

    /**
     * When a player creates a claim, if the automatic farmer is on, a farmer is added to the claim point.
     *
     * @param e Event of ClaimCrateEvent
     */
    @EventHandler
    public void createClaim(ClaimCreateEvent e){
        String claimId = e.getClaim().getID();
        if (Main.getConfigFile().getSettings().isAutoCreateFarmer()) {
            Farmer farmer = new Farmer(claimId, 0);
            ChatUtils.sendMessage(e.getSender(), Main.getLangFile().getMessages().getBoughtFarmer());
        }
    }

    /**
     * After a claimant adds a player to their territory, the added player is granted farmer access
     * @param e Event of TrustedPlayerEvent
     */
    @EventHandler
    public void trustPlayer(ClaimTrustEvent e){
        Claim claim = e.getClaim();
        if (!FarmerManager.getFarmers().containsKey(claim.getID())) return;
        UUID target = e.getTarget();
        Farmer farmer = FarmerManager.getFarmers().get(claim.getID());
        farmer.addUser(target,Bukkit.getOfflinePlayer(target).getName(), FarmerPerm.COOP);
    }

    /**
     * If the requester removes a trusted player from the demand zone, farmer access is terminated
     * @param e Event of UnTrustedPlayerEvent
     */
    @EventHandler
    public void unTrustPlayer(ClaimUnTrustEvent e){
        Claim claim = e.getClaim();
        if (!FarmerManager.getFarmers().containsKey(claim.getID())) return;
        Farmer farmer = FarmerManager.getFarmers().get(claim.getID());
        Optional<User> user = farmer.getUsersWithoutOwner().stream().filter(u -> u.getUuid().equals(e.getTarget())).findFirst();
        user.ifPresent(farmer::removeUser);
    }

    /**
     * When a claim is deleted, the farmer is removed as well
     * @param e Event of UnTrustedPlayerEvent
     */
    @EventHandler
    public void deleteClaim(ClaimDeleteEvent e){
        if (FarmerManager.farmers.containsKey(e.getClaim().getID())){
            FarmerAPI.getFarmerManager().removeFarmer(e.getClaim().getID(), e.getClaim().getOwner());
        }
    }

    /**
     * When the farmer is subsequently purchased, access is granted to all members in the Region.
     * @param e Event of buyFarmer
     */
    @EventHandler
    public void buyFarmer(FarmerBoughtEvent e) {
        String claimId = e.getFarmer().getRegionID();
        Claim claim = RClaimProvider.getClaimManager().getClaim(claimId);
        e.getFarmer().setRegionID(claimId);
        for (UUID member : claim.getMembers()) {
            e.getFarmer().addUser(member, Bukkit.getOfflinePlayer(member).getName(), FarmerPerm.COOP);
        }
    }
}
