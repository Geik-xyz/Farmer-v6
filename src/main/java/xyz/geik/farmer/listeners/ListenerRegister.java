package xyz.geik.farmer.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.listeners.backend.*;

/**
 * Register listener classes to spigot framework
 * on plugin start-up this constructor calls on Main#onEnable()
 */
public class ListenerRegister {

    /**
     * Listener Register constructor register listeners
     */
    public ListenerRegister() {
        register(new ItemEvent());
        register(new QuitEvent());
        register(new ChatEvent());
        register(new ItemSellEvent());
        register(new BuyFarmerEvent());
        register(new FastPlayerLookup());
    }

    /**
     * Shortcut of plugin manager
     * @param object of listener
     */
    private void register(Listener object) {
        Bukkit.getPluginManager().registerEvents(object, Main.getInstance());
    }
}
