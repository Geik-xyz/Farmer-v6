package xyz.geik.farmer.integrations.placeholderapi.expansion;

import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.integrations.placeholderapi.PlaceholderExecutor;

/**
 * FarmerLang expansion class
 *
 * @author Amowny
 * @since v6-b003
 */
public class FarmerLang extends PlaceholderExecutor {

    /**
     * Constructor for the FarmerLang placeholder
     */
    public FarmerLang() {
        super("getFarmerLang");
    }

    /**
     * Executes the FarmerLang placeholder.
     * @param player
     * @param string
     * @return farmer lang
     */
    public String execute(Player player, String string) {
        return Main.getConfigFile().getSettings().getLang();
    }
}
