package xyz.geik.farmer.integrations.askyblock;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import org.bukkit.Location;
import xyz.geik.farmer.integrations.Integrations;

import java.util.UUID;

/**
 * ASkyblock integration
 */
public class Askyblock extends Integrations {

    /**
     * Constructor register event of super class
     */
    public Askyblock() {super(new ASkyblockListener());}

    /**
     * Get owner UUID of region
     * @param regionID
     * @return
     */
    @Override
    public UUID getOwnerUUID(String regionID) {
        return UUID.fromString(regionID);
    }

    /**
     * Get owner UUID of location
     * @param location
     * @return
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        return ASkyBlockAPI.getInstance().getOwner(location);
    }

    /**
     * Get region ID of location
     * @param location
     * @return
     */
    @Override
    public String getRegionID(Location location) {
        return ASkyBlockAPI.getInstance().getOwner(location).toString();
    }
}
