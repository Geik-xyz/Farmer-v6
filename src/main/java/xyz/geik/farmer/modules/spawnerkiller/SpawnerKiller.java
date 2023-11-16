package xyz.geik.farmer.modules.spawnerkiller;

import lombok.Getter;
import org.bukkit.Bukkit;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.modules.FarmerModule;

import java.util.ArrayList;
import java.util.List;

/**
 * SpawnerKiller module main class
 * @author Geik
 */
@Getter
public class SpawnerKiller extends FarmerModule {

    /**
     * Constructor of class
     */
    public SpawnerKiller() {}

    /**
     * Whitelist of spawners
     */
    private List<String> whitelist = new ArrayList<>();

    /**
     * Blacklist of spawners
     */
    private List<String> blacklist = new ArrayList<>();

    private boolean requireFarmer = false, cookFoods = false, removeMob = true, defaultStatus = false;

    /**
     * Perm of spawner killer
     */
    private String customPerm = "farmer.spawnerkiller";

    /**
     * -- GETTER --
     *  Get instance of the module
     */
    @Getter
    private static SpawnerKiller instance;

    /**
     * onLoad method of module
     */
    @Override
    public void onLoad() {
        this.setName("SpawnerKiller");
        this.setDescription("Automatically kills spawner mobs");
        this.setModulePrefix("SpawnerKiller");
        this.setConfig(Main.getInstance());
        instance = this;
        if (!getConfig().getBoolean("settings.feature"))
            this.setEnabled(false);
    }

    /**
     * onEnable method of module
     */
    @Override
    public void onEnable() {
        setHasGui(true);
        defaultStatus = getConfig().getBoolean("settings.defaultStatus");
        customPerm = getConfig().getString("settings.customPerm");
        removeMob = getConfig().getBoolean("settings.removeMob");
        cookFoods = getConfig().getBoolean("settings.cookFoods");
        requireFarmer = getConfig().getBoolean("settings.requireFarmer");
        // SpawnerMeta Listener Register
        if (Bukkit.getPluginManager().getPlugin("SpawnerMeta") != null)
            new SpawnerMetaEvent();
        else
            registerListener(new SpawnerKillerEvent());
        registerListener(new SpawnerKillerGuiCreateEvent());
        setLang(Settings.lang, Main.getInstance());
        if (getConfig().contains("settings.whitelist"))
            getConfig().getTextList("settings.whitelist").forEach(whitelist::add);
        if (getConfig().contains("settings.blacklist"))
            getConfig().getTextList("settings.blacklist").forEach(blacklist::add);
    }

    /**
     * onReload method of module
     */
    @Override
    public void onReload() {
        if (!this.isEnabled())
            return;
        defaultStatus = getConfig().getBoolean("settings.defaultStatus");
        customPerm = getConfig().getString("settings.customPerm");
        removeMob = getConfig().getBoolean("settings.removeMob");
        cookFoods = getConfig().getBoolean("settings.cookFoods");
        requireFarmer = getConfig().getBoolean("settings.requireFarmer");
        if (!whitelist.isEmpty())
            whitelist.clear();
        if (!blacklist.isEmpty())
            blacklist.clear();
        if (getConfig().contains("settings.whitelist"))
            getConfig().getTextList("settings.whitelist").forEach(whitelist::add);
        if (getConfig().contains("settings.blacklist"))
            getConfig().getTextList("settings.blacklist").forEach(blacklist::add);
    }

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {

    }
}
