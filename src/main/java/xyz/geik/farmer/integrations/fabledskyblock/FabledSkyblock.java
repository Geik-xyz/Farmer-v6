package xyz.geik.farmer.integrations.fabledskyblock;

import com.songoda.skyblock.api.SkyBlockAPI;
import org.bukkit.Location;
import xyz.geik.farmer.integrations.Integrations;
import java.util.UUID;

/**
 * @author Heron4gf
 * @since v6-beta b9
 */
public class FabledSkyblock extends Integrations {
    /**
     * Constructor register event of super class
     */
    public FabledSkyblock() {super(new FabledListener());}

    /**
     * Get owner UUID of region
     * @param regionID
     * @return
     */
    @Override
    public UUID getOwnerUUID(String regionID) {
        return SkyBlockAPI.getIslandManager().getIslandByUUID(UUID.fromString(regionID)).getOwnerUUID();
    }

    /**
     * Get owner UUID of location
     * @param location
     * @return
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        return SkyBlockAPI.getIslandManager().getIslandAtLocation(location).getOwnerUUID();
    }

    /**
     * Get region ID of location
     * @param location
     * @return
     */
    @Override
    public String getRegionID(Location location) {
        return SkyBlockAPI.getIslandManager().getIslandAtLocation(location).getIslandUUID().toString();
    }
}
