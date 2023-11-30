package xyz.geik.farmer.integrations.placeholderapi.expansion;

import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.integrations.placeholderapi.PlaceholderExecutor;

/**
 * FarmerIsWorldAllowed expansion class
 *
 * @author Amowny
 * @since v6-b003
 */
public class FarmerIsWorldAllowed extends PlaceholderExecutor {

    /**
     * Constructor for the FarmerIsWorldAllowed placeholder
     */
    public FarmerIsWorldAllowed() {
        super("isWorldAllowed");
    }

    /**
     * Executes the FarmerIsWorldAllowed placeholder.
     * @param player
     * @param string
     * @return true or false
     */
    public String execute(Player player, String string) {
        if (Main.getConfigFile().getSettings().getAllowedWorlds().contains(string)) {
            return "true";
        }
        return "false";
    }
}
