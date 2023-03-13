package xyz.geik.farmer.api.managers;

import de.leonhard.storage.Config;
import de.leonhard.storage.Json;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;

/**
 * Uses SimplixStorage dependency to init config and json files
 */
public class StorageManager {

    public StorageManager() {}

    /**
     * Initiating config file
     *
     * @param fileName
     * @return
     */
    public Config initConfig(String fileName) {
        return initConfig(fileName, Main.getInstance());
    }

    /**
     * Initiating config file
     *
     * @param fileName
     * @return
     */
    public Config initLangFile(String fileName) {
        Config config = initConfig("lang/" + fileName, Main.getInstance());
        if (config == null)
            config = initConfig("lang/en", Main.getInstance());
        return config;
    }

    /**
     * Initiating config file for addons
     *
     * @param fileName
     * @param plugin
     * @return
     */
    public Config initConfig(String fileName, @NotNull JavaPlugin plugin) {
        fileName += ".yml";
        return new Config(fileName, "plugins/" + plugin.getDescription().getName(),
                Main.getInstance().getResource(fileName));
    }

    /**
     * Initiating json file
     *
     * @param fileName
     * @return
     */
    public Json initJson(String fileName) {
        return initJson(fileName, Main.getInstance());
    }

    /**
     * Initiating json file
     *
     * @param fileName
     * @return
     */
    public Json initJson(String fileName, @NotNull JavaPlugin plugin) {
        fileName += ".json";
        return new Json(fileName, "plugins/" + plugin.getDescription().getName(),
                Main.getInstance().getResource(fileName));
    }
}