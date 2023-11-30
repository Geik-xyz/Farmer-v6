package xyz.geik.farmer.integrations.lands;


import me.angeschossen.lands.api.LandsIntegration;
import org.bukkit.Location;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.integrations.Integrations;

import java.util.UUID;

/**
 * Lands integration class
 *
 * @author Amowny
 * @since v6-b003
 */
public class Lands extends Integrations {

    /**
     * Integrations#super calls here
     * Constructor for abstract class
     */
    public Lands() {
        super(new LandsListener());
    }

    /**
     * Lands API
     */
    LandsIntegration api = LandsIntegration.of(Main.getInstance());

    /**
     * Getting Owner UUID by Region ID
     */
    @Override
    public UUID getOwnerUUID(String regionID) {
        return api.getLandByName(regionID).getOwnerUID();
    }

    /**
     * Getting Owner UUID by Location of player
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        return api.getArea(location).getLand().getOwnerUID();
    }

    /**
     * Getting Region ID by Location of player
     */
    @Override
    public String getRegionID(Location location) {
        return UUID.fromString(api.getArea(location).getLand().getName()).toString();
    }
}
