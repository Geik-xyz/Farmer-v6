package xyz.geik.farmer.modules.geyser;

import lombok.Getter;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.farmer.modules.geyser.commands.GeyserCommand;
import xyz.geik.farmer.shades.storage.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Getter
    private static final HashMap<String,String> nameReplacer  = new HashMap<>();
    @Getter
    private static List<String> sellAllCommands  = new ArrayList<>();

    /**
     * onEnable method of module
     */
    public void onEnable() {
        instance = this;
        if (!Main.getModulesFile().getGeyser().isStatus())
            this.setEnabled(false);
        this.setLang(Main.getConfigFile().getSettings().getLang(), Main.getInstance());
        Main.getCommandManager().registerCommand(new GeyserCommand());
        // Adds all the replaces to the replacer
        if (!Main.getModulesFile().getGeyser().getSellAllCommands().isEmpty())
            sellAllCommands.addAll(Main.getModulesFile().getGeyser().getSellAllCommands());
        try {
            Geyser.getInstance().getLang().getTextList("sellReplace").forEach(element -> {
                String[] parts = element.split(":");
                nameReplacer.put(parts[0], parts[1]);
            });
        }
        catch (Exception ignored) {}
    }

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {}
}