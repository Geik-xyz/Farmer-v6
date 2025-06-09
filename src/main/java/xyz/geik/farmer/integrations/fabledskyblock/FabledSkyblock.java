package xyz.geik.farmer.integrations.fabledskyblock;

import com.songoda.skyblock.api.SkyBlockAPI;
import org.bukkit.Location;
import xyz.geik.farmer.integrations.Integrations;

import java.util.UUID;

/**
 * FabledSkyblock integration interface class
 *
 * @author mehmet-27
 */
public class FabledSkyblock extends Integrations {

    /**
     * Integrations#super calls here
     * Constructor for abstract class
     */
    public FabledSkyblock() {
        super(new FabledSkyListener());
    }

    /**
     * Get owner UUID of region
     */
    @Override
    public UUID getOwnerUUID(String regionID) {
        return SkyBlockAPI.getIslandManager().getIslandByUUID(UUID.fromString(regionID)).getOwnerUUID();
    }

    /**
     * Get owner UUID by location
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        return SkyBlockAPI.getIslandManager().getIslandAtLocation(location).getOwnerUUID();
    }

    /**
     * Get region ID by location
     */
    @Override
    public String getRegionID(Location location) {
        return SkyBlockAPI.getIslandManager().getIslandAtLocation(location).getIslandUUID().toString();
    }
}
