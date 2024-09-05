package xyz.geik.farmer.helpers;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * Parses placeholders if PlaceholderAPI plugin is enabled
 *
 * @author WaterArchery
 */
public class PlaceholderHelper {

    private static boolean isPlaceholderApiEnabled;

    // Initialize method on server load
    public static void initialize() {
        isPlaceholderApiEnabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    /**
     * Parses placeholders in the given string for the specified player.
     *
     * @param player The player for whom placeholders should be parsed.
     * @param s The string containing placeholders to be parsed.
     * @return The string with parsed placeholders if the plugin is enabled, otherwise the original string.
     */
    public static String parsePlaceholders(OfflinePlayer player, String s) {
        if (isPlaceholderApiEnabled) {
            return PlaceholderAPI.setPlaceholders(player, s);
        }

        return s;
    }
}
