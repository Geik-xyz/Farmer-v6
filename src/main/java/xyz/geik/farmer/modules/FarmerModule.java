package xyz.geik.farmer.modules;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.shades.storage.Config;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.module.GModule;

/**
 * Module system of Farmer
 * You can extend a class with
 * this class and register it
 *
 * @author Geyik
 */
@Getter
@Setter
public abstract class FarmerModule extends GModule {

    private Config lang;

    /**
     * Set default config of plugin
     * You may use it in your onLoad
     * or onEnable method of your module
     *
     * <p><b>IMPORTANT</b> file must be in
     * resources/lang folder, and it
     * must be named of lang as farmer.</p>
     *
     * @param langName name of lang
     * @param plugin for instance
     */
    public void setLang(String langName, JavaPlugin plugin) {
        lang = Main.getInstance().getSimplixStorageAPI()
                .initConfig("modules/" + this.getName().toLowerCase() + "/lang/" + langName, plugin);
    }
}