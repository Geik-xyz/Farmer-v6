package xyz.geik.farmer.integrations.ultimateclaims;

import org.bukkit.Location;
import xyz.geik.farmer.integrations.Integrations;

import java.util.UUID;

/**
 * UltimateClaims integration interface class
 *
 * @author Khontrom
 */
public class UltimateClaims extends Integrations {

    public UltimateClaims() { super(new UltimateListener()); }

    /**
     * Gets OwnerUUID by RegionID
     *
     * @param regionID id of region
     * @return UUID of owner
     */
    @Override
    public UUID getOwnerUUID(String regionID) {
        return com.craftaro.ultimateclaims.UltimateClaims.getInstance()
                .getClaimManager().getRegisteredClaims()
                .stream().filter(claim -> regionID.equals(String.valueOf(claim.getId())))
                .findFirst().get().getOwner().getUniqueId();
    }

    /**
     * Gets Owner UUID by Location
     *
     * @param location location of region
     * @return UUID of player
     */
    @Override
    public UUID getOwnerUUID(Location location) {
       return com.craftaro.ultimateclaims.UltimateClaims.getInstance().getClaimManager().getClaim(location.getChunk()).getOwner().getUniqueId();
    }

    /**
     * Gets RegionId by Location
     *
     * @param location of region
     * @return String of region id
     */
    @Override
    public String getRegionID(Location location) {
        return String.valueOf(com.craftaro.ultimateclaims.UltimateClaims.getInstance().getClaimManager().getClaim(location.getChunk()).getId());
    }
}
