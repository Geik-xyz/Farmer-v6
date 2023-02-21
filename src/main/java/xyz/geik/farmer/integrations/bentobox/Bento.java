package xyz.geik.farmer.integrations.bentobox;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import world.bentobox.bentobox.BentoBox;
import xyz.geik.farmer.integrations.Integrations;

import java.util.UUID;

public class Bento extends Integrations {

    /**
     * Constructor register event of super class
     *
     * @param listener
     */
    public Bento() {
        super(new BentoListener());
    }

    /**
     * Gets OwnerUUID by RegionID
     *
     * @param regionID
     * @return
     */
    @Override
    public UUID getOwnerUUID(String regionID) {
        return BentoBox.getInstance().getIslands().getIslandById(regionID).get().getOwner();
    }

    /**
     * Gets Owner UUID by Location
     *
     * @param location
     * @return
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        return BentoBox.getInstance().getIslands().getIslandAt(location).get().getOwner();
    }

    /**
     * Gets RegionId by Location
     *
     * @param location
     * @return
     */
    @Override
    public String getRegionID(Location location) {
        return BentoBox.getInstance().getIslands().getIslandAt(location).get().getUniqueId();
    }
}
