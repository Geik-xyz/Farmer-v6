package xyz.geik.farmer.integrations.superior;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.geik.farmer.integrations.Integrations;

import java.util.UUID;

/**
 * SuperiorSkyblock2 Integration hook
 * Which getting owner UUID and region ID
 * for necessary sections.
 */
public class SuperiorSkyblock extends Integrations {

    /**
     * Integrations#super calls here
     * Constructor for abstract class
     */
    public SuperiorSkyblock() {
        super(new SuperiorListener());
    }

    /**
     * Getting Owner UUID by Region ID
     */
    @Override
    public UUID getOwnerUUID(String regionId) {
        return SuperiorSkyblockAPI.getIslandByUUID(UUID.fromString(regionId)).getOwner().getUniqueId();
    }

    /**
     * Getting Owner UUID by Location of player
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        return SuperiorSkyblockAPI.getIslandAt(location).getOwner().getUniqueId();
    }

    /**
     * Getting Region ID by Location of player
     */
    @Override
    public String getRegionID(Location location) {
        return SuperiorSkyblockAPI.getIslandAt(location).getUniqueId().toString();
    }

    /**
     * Getting Region ID by Player
     */
    @Override
    public String getRegionIDWithPlayer(Player player) {
        return SuperiorSkyblockAPI.getPlayer(player.getUniqueId()).getIsland().getUniqueId().toString();
    }
}
