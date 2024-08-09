package xyz.geik.farmer.integrations.iridiumskyblock;

import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Location;
import xyz.geik.farmer.integrations.Integrations;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * IridiumSkyblock integration interface class
 *
 * @author Khontrom
 */
public class IridiumSkyblock extends Integrations {

    public IridiumSkyblock() { super(new IridiumListener());}

    /**
     * Gets OwnerUUID by RegionID
     *
     * @param regionID id of region
     * @return UUID of owner
     */
    @Override
    public UUID getOwnerUUID(String regionID) {
        Optional<Island> island = Objects.requireNonNull(IridiumSkyblockAPI.getInstance()).getIslandById(Integer.parseInt(regionID));
        Optional<User> user = island.map(Island::getOwner).orElse(null);
        return Objects.requireNonNull(user).map(User::getUuid).orElse(null);
    }
    /**
     * Gets Owner UUID by Location
     *
     * @param location location of region
     * @return UUID of player
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        Optional<Island> island = Objects.requireNonNull(IridiumSkyblockAPI.getInstance()).getIslandViaLocation(location);
        Optional<User> user = island.map(Island::getOwner).orElse(null);
        return Objects.requireNonNull(user).map(User::getUuid).orElse(null);
    }

    /**
     * Gets RegionId by Location
     *
     * @param location of region
     * @return String of region id
     */
    @Override
    public String getRegionID(Location location) {
        Optional<Island> island = Objects.requireNonNull(IridiumSkyblockAPI.getInstance()).getIslandViaLocation(location);
        Integer id = island.map(Island::getId).orElse(null);
        return id == null ? null : String.valueOf(id);
    }
}
