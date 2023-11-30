package xyz.geik.farmer.integrations.placeholderapi;

import org.bukkit.entity.Player;

/**
 * Placeholder executor class
 *
 * @author Amowny
 * @since v6-b003
 */
public abstract class PlaceholderExecutor {

    public static String NULL_KEY = "UNDEFINED_KEY";

    protected String identify;

    /**
     * Constructor
     * @param identify
     */
    public PlaceholderExecutor(String identify) {
        this.identify = identify;
    }

    /**
     * Get to identify
     * @param player
     * @param string
     * @return
     */
    public abstract String execute(Player player, String string);
}
