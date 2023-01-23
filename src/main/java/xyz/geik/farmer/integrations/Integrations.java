package xyz.geik.farmer.integrations;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.integrations.grief.GriefPrevent;
import xyz.geik.farmer.integrations.superior.SuperiorSkyblock;

import java.util.UUID;

/**
 * Abstract class for Integration hook
 * Which getting owner UUID and region ID
 * for necessary sections.
 */
public abstract class Integrations {

    /**
     * Constructor register event of super class
     */
    public Integrations(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, Main.getInstance());
    }

    /**
     * Getting Owner UUID by Region ID
     *
     * @param regionID
     */
    public abstract UUID getOwnerUUID(String regionID);

    /**
     * Getting Owner UUID by Location of player
     *
     * @param location
     */
    public abstract UUID getOwnerUUID(Location location);

    /**
     *
     * Getting Region ID by Location of player
     *
     * @param location
     */
    public abstract String getRegionID(Location location);

    /**
     * Catches plugin that server uses
     * and loads integration class of it.
     */
    public static void registerIntegrations() {
        if (Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2"))
            Main.setIntegration(new SuperiorSkyblock());
        else if (Bukkit.getPluginManager().isPluginEnabled("GriefPrevention"))
            Main.setIntegration(new GriefPrevent());
        else return;
    }
}
