package xyz.geik.farmer.integrations;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.integrations.askyblock.Askyblock;
import xyz.geik.farmer.integrations.bentobox.Bento;
import xyz.geik.farmer.integrations.fabledskyblock.FabledSkyblock;
import xyz.geik.farmer.integrations.grief.GriefPrevent;
import xyz.geik.farmer.integrations.lands.Lands;
import xyz.geik.farmer.integrations.superior.SuperiorSkyblock;
import xyz.geik.farmer.integrations.townyadvanced.TownyAdvanced;

import java.util.UUID;

/**
 * Abstract class for Integration hook
 * Which getting owner UUID and region ID
 * for necessary sections.
 *
 * @author geik
 */
public abstract class Integrations {

    /**
     * Constructor register event of super class
     * @param listener Listener class of integration
     */
    public Integrations(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, Main.getInstance());
    }

    /**
     * Getting Owner UUID by Region ID
     *
     * @param regionID id of region
     * @return UUID owner uuid
     */
    public abstract UUID getOwnerUUID(String regionID);

    /**
     * Getting Owner UUID by Location of player
     *
     * @param location location of region
     * @return UUID owner uuid
     */
    public abstract UUID getOwnerUUID(Location location);

    /**
     *
     * Getting Region ID by Location of player
     *
     * @param location location of region
     * @return String regionId
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
        else if (Bukkit.getPluginManager().isPluginEnabled("BentoBox"))
            Main.setIntegration(new Bento());
        else if (Bukkit.getPluginManager().isPluginEnabled("ASkyBlock"))
            Main.setIntegration(new Askyblock());
        else if(Bukkit.getPluginManager().isPluginEnabled("FabledSkyBlock"))
            Main.setIntegration(new FabledSkyblock());
        else if (Bukkit.getPluginManager().isPluginEnabled("Towny"))
            Main.setIntegration(new TownyAdvanced());
        else if (Bukkit.getPluginManager().isPluginEnabled("Lands"))
            Main.setIntegration(new Lands());
    }
}
