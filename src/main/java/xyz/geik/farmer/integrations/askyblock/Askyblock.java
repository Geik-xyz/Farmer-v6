package xyz.geik.farmer.integrations.askyblock;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import org.bukkit.Location;
import xyz.geik.farmer.integrations.Integrations;

import java.util.UUID;

/**
 * ASky-block integration
 * @author poyraz
 * @since 1.0.0
 */
public class Askyblock extends Integrations {

    /**
     * Constructor register event of super class
     */
    public Askyblock() {super(new ASkyblockListener());}

    /**
     * Get owner UUID of region
     * @param regionID id of region
     * @return UUID of owner
     */
    @Override
    public UUID getOwnerUUID(String regionID) {
        return UUID.fromString(regionID);
    }

    /**
     * Get owner UUID of location
     * @param location of region
     * @return UUID of owner
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        return ASkyBlockAPI.getInstance().getOwner(location);
    }

    /**
     * Get region ID of location
     * @param location location of region
     * @return String id of region
     */
    @Override
    public String getRegionID(Location location) {
        return ASkyBlockAPI.getInstance().getOwner(location).toString();
    }

}
