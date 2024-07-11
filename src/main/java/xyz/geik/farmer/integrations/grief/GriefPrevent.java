package xyz.geik.farmer.integrations.grief;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.integrations.Integrations;

import java.util.UUID;

/**
 * GriefPrevention Integration hook
 * Which getting owner UUID and region ID
 * for necessary sections.
 */
public class GriefPrevent extends Integrations {

    /**
     * Hooking GriefPrevention main class
     * Because i couldn't find any developer API
     * About this plugin so i get through
     * Main class.
     */
    public static GriefPrevention grief = Main.getPlugin(GriefPrevention.class);

    /**
     * Integrations#super calls here
     * Constructor for abstract class
     */
    public GriefPrevent() {
        super(new GriefListener());
    }

    /**
     * Getting Owner UUID by Region ID
     */
    @Override
    public UUID getOwnerUUID(String regionId) {
        return grief.dataStore.getClaim(Long.parseLong(regionId)).ownerID;
    }

    /**
     * Getting Owner UUID by Location of player
     */
    @Override
    public UUID getOwnerUUID(Location location) {
        return grief.dataStore.getClaimAt(location, true, null).ownerID;
    }

    /**
     * Getting Region ID by Location of player
     */
    @Override
    public String getRegionID(Location location) {
        return grief.dataStore.getClaimAt(location, true, null).getID().toString();
    }
}
