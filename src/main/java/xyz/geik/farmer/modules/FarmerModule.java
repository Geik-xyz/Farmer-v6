package xyz.geik.farmer.modules;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.modules.production.Production;
import xyz.geik.farmer.modules.voucher.Voucher;

@Getter
@Setter
public abstract class FarmerModule {

    protected String name;
    protected boolean isEnabled;
    protected String description;
    protected String modulePrefix;

    public FarmerModule(String name, boolean isEnabled, String description, String modulePrefix) {
        this.name = name;
        this.isEnabled = isEnabled;
        this.description = description;
        this.modulePrefix = modulePrefix;
    }

    public abstract void registerListeners();

    public abstract void onEnable();

    public void registerListener(Listener listener) {
        PluginManager pm = Main.getInstance().getServer().getPluginManager();
        Main.getInstance().getListenerList().put(this, listener);
        pm.registerEvents(listener, Main.getInstance());
        System.out.println(" [LISTENERS] " + listener.getClass().getSimpleName() + " was registered");
    }

    public static void registerModules(){
        ModuleManager moduleManager = Main.getInstance().getModuleManager();
        moduleManager.registerModule(new Voucher());
        moduleManager.registerModule(new Production());
        moduleManager.loadModules();
    }
}