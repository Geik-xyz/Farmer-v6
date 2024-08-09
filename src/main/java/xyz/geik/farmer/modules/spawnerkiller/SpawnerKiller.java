package xyz.geik.farmer.modules.spawnerkiller;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.farmer.modules.spawnerkiller.handlers.SpawnerKillerEvent;
import xyz.geik.farmer.modules.spawnerkiller.handlers.SpawnerKillerGuiCreateEvent;
import xyz.geik.farmer.modules.spawnerkiller.handlers.SpawnerMetaEvent;
import xyz.geik.farmer.shades.storage.Config;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SpawnerKiller extends FarmerModule {

    /**
     * Constructor of class
     */
    public SpawnerKiller() {}

    @Getter
    private static SpawnerKiller instance;

    private static SpawnerKillerEvent spawnerKillerEvent;

    private static SpawnerKillerGuiCreateEvent spawnerKillerGuiCreateEvent;

    private final List<String> whitelist = new ArrayList<>();

    private final List<String> blacklist = new ArrayList<>();

    private boolean requireFarmer = false, cookFoods = false, removeMob = true, defaultStatus = false;

    private String customPerm = "farmer.spawnerkiller";

    private Config langFile;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        instance = this;
        this.setLang(Main.getConfigFile().getSettings().getLang(), Main.getInstance());
        this.setHasGui(true);
        spawnerKillerEvent = new SpawnerKillerEvent();
        spawnerKillerGuiCreateEvent = new SpawnerKillerGuiCreateEvent();
        if (Bukkit.getPluginManager().getPlugin("SpawnerMeta") != null)
            new SpawnerMetaEvent();
        else
            Bukkit.getPluginManager().registerEvents(spawnerKillerEvent, Main.getInstance());
        Bukkit.getPluginManager().registerEvents(spawnerKillerGuiCreateEvent, Main.getInstance());
        defaultStatus = Main.getModulesFile().getSpawnerKiller().isDefaultStatus();
        customPerm = Main.getModulesFile().getSpawnerKiller().getCustomPerm();
        removeMob = Main.getModulesFile().getSpawnerKiller().isRemoveMob();
        cookFoods = Main.getModulesFile().getSpawnerKiller().isCookFoods();
        requireFarmer = Main.getModulesFile().getSpawnerKiller().isRequireFarmer();
        if (Main.getModulesFile().getSpawnerKiller().getMode().equals("whitelist"))
            whitelist.addAll(Main.getModulesFile().getSpawnerKiller().getWhitelist());
        if (Main.getModulesFile().getSpawnerKiller().getMode().equals("blacklist"))
            blacklist.addAll(Main.getModulesFile().getSpawnerKiller().getBlacklist());
    }

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {
        HandlerList.unregisterAll(spawnerKillerEvent);
        HandlerList.unregisterAll(spawnerKillerGuiCreateEvent);
    }
}