package xyz.geik.farmer.helpers;

import xyz.geik.farmer.Main;

import java.util.ArrayList;
import java.util.List;


public class WorldHelper {

    // Allowed world array loaded static for one time management
    public static List<String> allowedWorlds = new ArrayList<>();
    /**
     * Loads all the allowed worlds to cache
     *
     * @author poyraz.inan
     * @since b107
     */
    public static void loadAllowedWorlds() {
        allowedWorlds = new ArrayList<>(Main.getConfigFile().getSettings().getAllowedWorlds());
    }

    /**
     * Checks if Farmer is allowed in a specified world.
     *
     * @param worldName   The name of the world to check.
     * @return            if Farmer is allowed in the specified world
     */
    public static boolean isFarmerAllowed(String worldName) {
        // Retrieve the list of allowed worlds from the configuration.

        for (String world : allowedWorlds) {
            // Check if the current world uses a wildcard.
            if (world.equals(worldName)) {
                return true;
            }
            // Performance improvement
            else if (Main.getConfigFile().getSettings().isAllowedWorldsStarCharFeature()) {
                if (world.contains("*")) {
                    // Remove the wildcard for partial matching.
                    world = world.replace("*", "");
                    // Check if the worldName contains the current world (after removing the wildcard).
                    if (worldName.contains(world)) return true;
                }
            }
        }

        // Return false if no match is found.
        return false;
    }

}
