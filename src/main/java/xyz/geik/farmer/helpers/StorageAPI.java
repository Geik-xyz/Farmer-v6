package xyz.geik.farmer.helpers;

import de.leonhard.storage.Config;
import de.leonhard.storage.Json;
import xyz.geik.farmer.Main;

public class StorageAPI {

    private static String prefix = null;

    public StorageAPI() {
    }

    public Config initConfig(String fileName) {
        fileName += ".yml";
        return new Config(fileName, "plugins/" + Main.getInstance().getDescription().getName(),
                Main.getInstance().getResource(fileName));
    }

    public Json initJson(String fileName) {
        fileName += ".json";
        return new Json(fileName, "plugins/" + Main.getInstance().getDescription().getName(),
                Main.getInstance().getResource(fileName));
    }

    public void setPrefix(String prefix) {
        StorageAPI.prefix = prefix;
    }

    public String replacePrefix(String text) {
        if (StorageAPI.prefix == null)
            return text;

        prefix = Main.color(prefix);
        return text.replaceAll("%prefix%", prefix);
    }

    public String replacePlayer(String text, String player) {
        return text.replaceAll("%player%", player);
    }

    public String getText(Config config, String key) {
        return replacePrefix(config.getText(key));
    }

    public String getText(Json json, String key) {
        return replacePrefix(json.getText(key));
    }
}
