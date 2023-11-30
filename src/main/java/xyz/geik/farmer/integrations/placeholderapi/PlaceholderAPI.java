package xyz.geik.farmer.integrations.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.integrations.placeholderapi.expansion.*;

import java.util.HashMap;
import java.util.Map;

/**
 * PlaceholderAPI integration class
 *
 * @author amownyy
 * @since v6-b003
 */
public class PlaceholderAPI extends PlaceholderExpansion {

    /**
     * Get special identifier
     * @return identifier
     */
    @NotNull
    public String getIdentifier() {
        return "farmer";
    }

    /**
     * Get author
     * @return author
     */
    @NotNull
    public String getAuthor() {
        return "Geik";
    }

    /**
     * Get version
     * @return version
     */
    @NotNull
    public String getVersion() {
        return "v6-b002";
    }

    private static Map<String, PlaceholderExecutor> executors = new HashMap<>();


    /**
     * Constructor of class
     */
    public PlaceholderAPI() {
        addExecutors(new PlaceholderExecutor[] {
                new FarmerBuyStatus(),
                new FarmerCount(),
                new FarmerIsWorldAllowed(),
                new FarmerLang(),
                new FarmerPrice(),
                new FarmerStatus()
        });
    }

    /**
     * Add placeholder executor
     * @param executor
     */
    private void addExecutor(PlaceholderExecutor executor) {
        executors.put(executor.identify, executor);
    }

    /**
     * Add placeholder executor list
     * @param executorList
     */
    private void addExecutors(PlaceholderExecutor... executorList) {
        for (PlaceholderExecutor placeholderExecutor : executorList)
            addExecutor(placeholderExecutor);
    }

    /**
     * Get placeholder value
     * @param player
     * @param params
     * @return placeholder value or UNDEFINED_PLACEHOLDER
     */
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.contains(":") || (params.startsWith("get") || params.startsWith("has"))) {
            String[] data = params.split(":");
            String identify = data[0];
            if (executors.containsKey(identify)) {
                PlaceholderExecutor placeholderHandler = executors.get(identify);
                String executorParams = params.replace(identify + ":", "");
                return placeholderHandler.execute(player, executorParams);
            }
        }
        return "UNDEFINED_PLACEHOLDER";
    }

}
