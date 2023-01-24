package xyz.geik.farmer.helpers;

import de.leonhard.storage.Config;
import de.leonhard.storage.Json;
import xyz.geik.farmer.Main;

/**
 * SimplixStorage dependency used here
 * Simply init config, json
 */
public class StorageAPI {

    public StorageAPI() {}

    /**
     * Initiating config file
     *
     * @param fileName
     * @return
     */
    public Config initConfig(String fileName) {
        fileName += ".yml";
        return new Config(fileName, "plugins/" + Main.getInstance().getDescription().getName(),
                Main.getInstance().getResource(fileName));
    }

    /**
     * Initiating json file
     *
     * @param fileName
     * @return
     */
    public Json initJson(String fileName) {
        fileName += ".json";
        return new Json(fileName, "plugins/" + Main.getInstance().getDescription().getName(),
                Main.getInstance().getResource(fileName));
    }
}
