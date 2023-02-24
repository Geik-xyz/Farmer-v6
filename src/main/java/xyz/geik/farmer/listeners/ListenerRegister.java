package xyz.geik.farmer.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.listeners.backend.ChatEvent;
import xyz.geik.farmer.listeners.backend.ProductionCalculateEvent;
import xyz.geik.farmer.listeners.backend.ItemEvent;
import xyz.geik.farmer.listeners.backend.QuitEvent;

/**
 * Register listener classes to spigot framework
 * on plugin start-up this constructor calls on Main#onEnable()
 */
public class ListenerRegister {

    public ListenerRegister() {
        register(new ItemEvent());
        register(new QuitEvent());
        register(new ChatEvent());
        if (Settings.hasAnyProductionCalculating)
            register(new ProductionCalculateEvent());
    }

    /**
     * Shortcut of pluginmanager
     * @param object
     */
    private void register(Listener object) {
        Bukkit.getPluginManager().registerEvents(object, Main.getInstance());
    }
}
