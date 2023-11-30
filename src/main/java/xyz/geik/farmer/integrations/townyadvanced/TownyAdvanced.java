package xyz.geik.farmer.integrations.townyadvanced;

import com.palmergames.bukkit.towny.TownyAPI;
import org.bukkit.Location;
import xyz.geik.farmer.integrations.Integrations;

import java.util.UUID;

/**
 * TownyAdvanced integration class
 *
 * @author amownyy
 * @since v6-b001
 */
public class TownyAdvanced extends Integrations {

    /**
     * Integrations#super calls here
     * Constructor for abstract class
     */
    public TownyAdvanced() {
        super(new TownyListener());
    }

    /**
     * Getting Owner UUID by Region ID
     */
    @Override
    public UUID getOwnerUUID(String regionID) {
        return TownyAPI.getInstance().getTown(UUID.fromString(regionID)).getMayor().getUUID();
    }

    /**
     * Getting Owner UUID by Location of player
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        return TownyAPI.getInstance().getTown(location).getMayor().getUUID();
    }

    /**
     * Getting Region ID by Location of player
     */
    @Override
    public String getRegionID(Location location) {
        if (TownyAPI.getInstance().getTown(location) == null)
            return null;
        return TownyAPI.getInstance().getTown(location).getUUID().toString();
    }
}
