package xyz.geik.farmer.modules;

import org.bukkit.event.HandlerList;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.modules.exceptions.ReloadModuleException;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {


    protected List<FarmerModule> farmerModuleList = new ArrayList<>();


    public List<FarmerModule> getModuleList() {
        return farmerModuleList;
    }

    public void registerModule(FarmerModule farmerModule) {
        farmerModuleList.add(farmerModule);
    }

    public void removeModule(FarmerModule farmerModule) {
        farmerModuleList.remove(farmerModule);
    }

    public void loadModules() {
        for (FarmerModule farmerModule : Main.getInstance().getModuleManager().getModuleList()) {
            if (!farmerModule.isEnabled()) {
                System.out.println(" [MODULES] " + farmerModule.getName() + " was not registered");
            } else {
                System.out.println(" [MODULES] " + farmerModule.getName() + " was registered");
                farmerModule.onEnable();
                farmerModule.registerListeners();
            }
        }
    }


    public void reloadModule(FarmerModule farmerModule) throws ReloadModuleException {
        getModuleList().remove(farmerModule);
        close(farmerModule);
        try {
            registerModule(farmerModule.getClass().newInstance());
        } catch (Exception e) {
            throw new ReloadModuleException(e.getMessage());
        }
        System.out.println(farmerModule.getName() + " was reloaded. It is: " + farmerModule.isEnabled);
    }


    public FarmerModule getByName(String module) {
        FarmerModule farmerModuleSelected = null;
        for (FarmerModule mods : getModuleList()) {
            if (mods.getName().equals(module)) {
                farmerModuleSelected = mods;
                return farmerModuleSelected;
            }
        }
        throw new NullPointerException("Module cannot be found");
    }

    public void close(FarmerModule farmerModule) {
        HandlerList handlerList = new HandlerList();
        handlerList.unregister(Main.getInstance().getListenerList().get(farmerModule));
        Main.getInstance().getListenerList().remove(farmerModule);
    }
}