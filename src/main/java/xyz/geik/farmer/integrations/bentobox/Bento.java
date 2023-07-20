package xyz.geik.farmer.integrations.bentobox;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.integrations.Integrations;

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
        return BentoBox.getInstance().getIslands().getIslandById(regionID).get().getOwner();
    }

    /**
     * Gets Owner UUID by Location
     *
     * @param location location of region
     * @return UUID of player
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        return BentoBox.getInstance().getIslands().getIslandAt(location).get().getOwner();
    }

    /**
     * Gets RegionId by Location
     *
     * @param location of region
     * @return String of region id
     */
    @Override
    public String getRegionID(Location location) {
        return BentoBox.getInstance().getIslands().getIslandAt(location).get().getUniqueId();
    }

    /**
     * Gets RegionId by Player
     *
     * @param player of region
     * @return String of region id
     */
    @Override
    public String getRegionIDWithPlayer(Player player) {
        return BentoBox.getInstance().getIslands().getIsland(Bukkit.getWorld(Main.getConfigFile().getString("settings.bentoboxWorld")), player.getUniqueId()).getUniqueId();
    }
}
