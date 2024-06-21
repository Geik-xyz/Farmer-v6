package xyz.geik.farmer.integrations.bentobox;

import org.bukkit.Location;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;
import xyz.geik.farmer.integrations.Integrations;

import java.util.Optional;
import java.util.UUID;

/**
 * Bentobox integration
 * @author poyraz
 * @since 1.0.0
 */
public class Bento extends Integrations {

    /**
     * Constructor register event of super class
     */
    public Bento() {
        super(new BentoListener());
    }

    /**
     * Gets OwnerUUID by RegionID
     *
     * @param regionID id of region
     * @return UUID of owner
     */
    @Override
    public UUID getOwnerUUID(String regionID) {
        Optional<Island> island = BentoBox.getInstance().getIslands().getIslandById(regionID);
        return island.map(Island::getOwner).orElse(null);
    }

    /**
     * Gets Owner UUID by Location
     *
     * @param location location of region
     * @return UUID of player
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        Optional<Island> island = BentoBox.getInstance().getIslands().getIslandAt(location);
        return island.map(Island::getOwner).orElse(null);
    }

    /**
     * Gets RegionId by Location
     *
     * @param location of region
     * @return String of region id
     */
    @Override
    public String getRegionID(Location location) {
        Optional<Island> island = BentoBox.getInstance().getIslands().getIslandAt(location);
        return island.map(Island::getUniqueId).orElse(null);
    }
}
