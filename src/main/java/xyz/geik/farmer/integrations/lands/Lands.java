package xyz.geik.farmer.integrations.lands;

import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.applicationframework.util.ULID;
import me.angeschossen.lands.api.land.Land;
import org.bukkit.Location;
import org.bukkit.World;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.integrations.Integrations;

import java.util.UUID;

/**
 * Lands integration class
 *
 * @author amownyy
 * @since v6-b003
 */
public class Lands extends Integrations {

    /**
     * Integrations#super calls here
     * Constructor for abstract class
     */
    public Lands() {
        super(new LandsListener());
        api = LandsIntegration.of(Main.getInstance());
    }

    /**
     * Lands API
     */
    LandsIntegration api;

    /**
     * Getting Owner UUID by Region ID
     */
    @Override
    public UUID getOwnerUUID(String regionID) {
        ULID ulid = ULID.fromString(regionID);
        Land land = api.getLandByULID(ulid);
        return land == null ? null : land.getOwnerUID();
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
        World world = location.getWorld();
        int chunkX = location.getChunk().getX();
        int chunkZ = location.getChunk().getZ();

        Land land = api.getLandByChunk(world, chunkX, chunkZ);
        return land == null ? null : land.getULID().toString();
    }
}
