package xyz.geik.farmer.integrations.placeholderapi.expansion;

import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.integrations.placeholderapi.PlaceholderExecutor;

/**
 * FarmerStatus expansion class
 *
 * @author Amowny
 * @since v6-b003
 */
public class FarmerStatus extends PlaceholderExecutor {

    /**
     * Constructor for the FarmerStatus placeholder
     */
    public FarmerStatus() {
        super("getFarmerStatus");
    }

    public String execute(Player player, String string) {
        String regionID = getRegionID(player);
        if (regionID != null) {
            return Main.getLangFile().getText("noRegion");
        } else if (!FarmerManager.getFarmers().containsKey(regionID)) {
            return "false";
        }
        return "true";
    }

    /**
     * Gets region id with #Integration
     * if there has a region.
     *
     * @param player the command executor
     * @return String of region
     */
    private String getRegionID(Player player) {
        String regionID;
        // Simple try catch method for
        // compatibility with all plugins
        try {
            regionID = Main.getIntegration().getRegionID(player.getLocation());
        }
        catch (Exception e) {
            regionID = null;
        }
        return regionID;
    }
}
