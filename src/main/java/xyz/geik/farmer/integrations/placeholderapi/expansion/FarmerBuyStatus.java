package xyz.geik.farmer.integrations.placeholderapi.expansion;

import org.bukkit.entity.Player;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.integrations.placeholderapi.PlaceholderExecutor;

/**
 * FarmerBuyStatus expansion class
 *
 * @author Amowny
 * @since v6-b003
 */
public class FarmerBuyStatus extends PlaceholderExecutor {

    /**
     * Constructor for the FarmerBuyStatus placeholder
     */
    public FarmerBuyStatus() {
        super("getFarmerBuyStatus");
    }

    /**
     * Executes the FarmerBuyStatus placeholder.
     * @param player
     * @param string
     * @return true or false
     */
    public String execute(Player player, String string) {
        if (Settings.buyFarmer) {
            return "true";
        }
        return "false";
    }
}
