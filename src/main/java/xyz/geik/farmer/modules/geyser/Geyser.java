package xyz.geik.farmer.modules.geyser;

import lombok.Getter;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.farmer.shades.storage.Config;

/**
 * Geyser module main class
 */
@Getter
public class Geyser extends FarmerModule {

    /**
     * Constructor of class
     */
    public Geyser() {}

    @Getter
    private static Geyser instance;

    private Config langFile;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        instance = this;
        if (!Main.getModulesFile().getGeyser().isStatus())
            this.setEnabled(false);
        this.setLang(Main.getConfigFile().getSettings().getLang(), Main.getInstance());
    }

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {}
}