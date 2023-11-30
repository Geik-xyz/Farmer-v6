package xyz.geik.farmer.integrations.placeholderapi.expansion;


import org.bukkit.entity.Player;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.integrations.placeholderapi.PlaceholderExecutor;

/**
 * FarmerCount expansion class
 *
 * @author Amowny
 * @since v6-b003
 */
public class FarmerCount extends PlaceholderExecutor {

    /**
     * Constructor for the FarmerCount placeholder
     */
    public FarmerCount() {
        super("getFarmerCount");
    }

    /**
     * Executes the FarmerCount placeholder.
     * @param player
     * @param string
     * @return farmer count on the server
     */
    public String execute(Player player, String string) {
        return FarmerManager.getFarmers().size() + "";
    }
}
