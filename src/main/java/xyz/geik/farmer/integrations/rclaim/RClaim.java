package xyz.geik.farmer.integrations.rclaim;

import net.weesli.rClaim.api.RClaimAPI;
import net.weesli.rClaim.utils.Claim;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import xyz.geik.farmer.integrations.Integrations;

import java.util.Optional;
import java.util.UUID;

public class RClaim extends Integrations {
    /**
     * Constructor register event of super class
     *
     */
    public RClaim() {super(new RClaimListener());}

    /**
     *
     * @param regionID id of region
     * @return
     */
    @Override
    public UUID getOwnerUUID(String regionID) {
        return RClaimAPI.getInstance().getClaim(regionID).getOwner();
    }

    /**
     *
     * @param location location of region
     * @return
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        Claim claim = RClaimAPI.getInstance().getClaim(location.getChunk());
        if (claim != null){
            return claim.getOwner();
        }
        return UUID.randomUUID();
    }

    /**
     *
     * @param location location of region
     * @return
     */
    @Override
    public String getRegionID(Location location) {
        Claim claim = RClaimAPI.getInstance().getClaim(location.getChunk());
        Optional<Claim> center_claim = RClaimAPI.getInstance().getClaims().stream().filter(c -> c.isOwner(claim.getOwner())).filter(Claim::isCenter).findFirst();
        if (center_claim.isPresent()){
            return center_claim.get().getID();
        }
        return "";
    }
}
