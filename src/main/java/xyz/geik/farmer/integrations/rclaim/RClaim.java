package xyz.geik.farmer.integrations.rclaim;

import net.weesli.rclaim.api.RClaimProvider;
import net.weesli.rclaim.api.model.Claim;
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
        return RClaimProvider.getClaimManager().getClaim(regionID).getOwner();
    }

    /**
     * Gets UUID of owner from RClaimAPI by location
     *
     * @param location location of region
     * @return UUID of region owner
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        Claim claim = RClaimProvider.getClaimManager().getClaim(location);
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
        Claim claim = RClaimProvider.getClaimManager().getClaim(location);
        return claim.getID();
    }
}
