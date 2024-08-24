package xyz.geik.farmer.integrations.rclaim;

import net.weesli.rClaim.api.RClaimAPI;
import net.weesli.rClaim.utils.Claim;
import org.bukkit.Location;
import xyz.geik.farmer.integrations.Integrations;

import java.util.Optional;
import java.util.UUID;

/**
 * Main RClaim integration class that extends Integration
 *
 * @author Weesli
 * @since 25.08.2024
 */
public class RClaim extends Integrations {

    /**
     * Constructor register event of super class
     *
     */
    public RClaim() {super(new RClaimListener());}

    /**
     * Gets UUID of owner from RClaimAPI by regionId
     *
     * @param regionID id of region
     * @return UUID of region owner
     */
    @Override
    public UUID getOwnerUUID(String regionID) {
        return RClaimAPI.getInstance().getClaim(regionID).getOwner();
    }

    /**
     * Gets UUID of owner from RClaimAPI by location
     *
     * @param location location of region
     * @return UUID of region owner
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        Claim claim = RClaimAPI.getInstance().getClaim(location.getChunk());
        if (claim != null){
            return claim.getOwner();
        }
        return null;
    }

    /**
     * Gets getRegionID of location
     *
     * @param location location of region
     * @return id of region
     */
    @Override
    public String getRegionID(Location location) {
        Claim claim = RClaimAPI.getInstance().getClaim(location.getChunk());
        Optional<Claim> center_claim = RClaimAPI.getInstance().getClaims().stream().filter(c -> c.isOwner(claim.getOwner())).filter(Claim::isCenter).findFirst();
        return center_claim.map(Claim::getID).orElse(null);
    }
}
