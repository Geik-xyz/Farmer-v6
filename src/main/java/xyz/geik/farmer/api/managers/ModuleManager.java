package xyz.geik.farmer.api.managers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.modules.FarmerModule;
import xyz.geik.farmer.modules.exceptions.ModuleExistException;
import xyz.geik.farmer.modules.exceptions.ReloadModuleException;
import xyz.geik.farmer.modules.spawnerkiller.SpawnerKiller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Module Manager
 *
 * @author Geyik
 */
@Getter
@Setter
public class ModuleManager {

    // Module list of Farmer
    private List<FarmerModule> moduleList = new ArrayList<>();

    private boolean isModulesUseGui = false;

    /**
     * Register module registering only adds queue to load.
     * Use addModule to force load module.
     * This method is used for module load on startup.
     *
     * @param farmerModule
     */
    public void registerModule(@NotNull FarmerModule farmerModule) {
        farmerModule.onLoad();
        getModuleList().add(farmerModule);
    }

    /**
     * Add module after module load.
     * Force load module.
     *
     * @param farmerModule
     */
    public void addModule(FarmerModule farmerModule) throws ModuleExistException {
        if (getModuleList().contains(farmerModule))
            throw new ModuleExistException("Module " + farmerModule.getName() + " is already loaded!", farmerModule);
        else
            registerModule(farmerModule);
        loadModule(farmerModule);
    }

    /**
     * Remove module
     * @param farmerModule
     */
    public void removeModule(@NotNull FarmerModule farmerModule) {
        farmerModule.setEnabled(false);
        close(farmerModule);
        getModuleList().remove(farmerModule);
        calculateModulesUseGui();
    }

    /**
     * Reload module
     *
     * @param farmerModule
     * @throws ReloadModuleException
     */
    public void reloadModule(FarmerModule farmerModule) throws ReloadModuleException {
        getModuleList().remove(farmerModule);
        close(farmerModule);
        try {
            registerModule(farmerModule.getClass().newInstance());
        } catch (Exception e) {
            throw new ReloadModuleException(e.getMessage());
        }
        Bukkit.getConsoleSender().sendMessage(Main.color("&2[FarmerModules] &a" + farmerModule.getName() +
                " was reloaded it is: " + farmerModule.isEnabled()));
    }


    /**
     * Gets module by name which is registered in Farmer
     *
     * @param module
     * @return
     */
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

    /**
     * Close module
     * @param farmerModule
     */
    public void close(@NotNull FarmerModule farmerModule) {
        farmerModule.onDisable();
        HandlerList handlerList = new HandlerList();
        handlerList.unregister(Main.getInstance().getListenerList().get(farmerModule));
        Main.getInstance().getListenerList().remove(farmerModule);
    }

    /**
     * Load all modules in array list.
     */
    public void loadModules() {
        for (FarmerModule farmerModule : FarmerAPI.getModuleManager().getModuleList()) {
            loadModule(farmerModule);
        }
    }

    /**
     * Load modules handler method
     */
    private void loadModule(@NotNull FarmerModule farmerModule) {
        if (!farmerModule.isEnabled())
            Bukkit.getConsoleSender().sendMessage(Main.color("&4[FarmerModules] &c" + farmerModule.getName() + " disabled"));
        else {
            Bukkit.getConsoleSender().sendMessage(Main.color("&2[FarmerModules] &a" + farmerModule.getName() + " enabled"));
            farmerModule.onEnable();
            if (farmerModule.isHasGui() && !isModulesUseGui)
                isModulesUseGui = true;
        }

    }

    /**
     * Gets attribute from Farmer
     *
     * @param attribute
     * @param farmer
     * @return
     */
    public boolean getAttributeStatus(String attribute, @NotNull Farmer farmer) {
        if (farmer.getModuleAttributes().containsKey(attribute))
            return farmer.getModuleAttributes().get(attribute);
        else return SpawnerKiller.getInstance().isDefaultStatus();
    }

    /**
     * Change attribute
     *
     * @param attribute
     * @param farmer
     * @return
     */
    public boolean changeAttribute(String attribute, @NotNull Farmer farmer) {
        if (farmer.getModuleAttributes().containsKey(attribute)) {
            farmer.getModuleAttributes().remove(attribute);
            return SpawnerKiller.getInstance().isDefaultStatus();
        }
        else {
            boolean status = !SpawnerKiller.getInstance().isDefaultStatus();
            farmer.getModuleAttributes().put(attribute, status);
            return status;
        }
    }

    /**
     * Save attributes to database
     *
     * @param con
     * @param farmer
     * @throws SQLException
     */
    public void databaseUpdateAttribute(@NotNull Connection con, @NotNull Farmer farmer) throws SQLException {
        final String SQL_QUERY = "UPDATE Farmers SET attributes = ? WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(SQL_QUERY)) {
            statement.setString(1, attributeSerializer(farmer.getModuleAttributes()));
            statement.setInt(2, farmer.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Get attributes from database
     *
     * @param con
     * @param farmer
     * @throws SQLException
     */
    public void databaseGetAttributes(Connection con, @NotNull Farmer farmer) throws SQLException {
        final String SQL_QUERY = "SELECT attributes FROM Farmers WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(SQL_QUERY)) {
            statement.setInt(1, farmer.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                farmer.setModuleAttributes(attributeDeserializer(resultSet.getString("attributes")));
            else farmer.setModuleAttributes(new HashMap<>());
        }
    }

    /**
     * Serialize attribute data to save in database
     *
     * @param attributes
     * @return
     */
    private @Nullable String attributeSerializer(@NotNull HashMap<String, Boolean> attributes) {
        if (attributes.isEmpty())
            return null;
        StringBuilder builder = new StringBuilder();
        for (String key : attributes.keySet())
            builder.append(key).append(":").append(attributes.get(key)).append(";");

        if (builder.toString().isEmpty())
            return null;
        else
            return builder.toString();
    }

    /**
     * Deserialize attribute data from database
     *
     * @param attributes
     * @return
     */
    private @NotNull HashMap<String, Boolean> attributeDeserializer(@NotNull String attributes) {
        HashMap<String, Boolean> map = new HashMap<>();
        if (attributes == null || attributes.isBlank())
            return map;
        for (String attribute : attributes.split(";"))
            if (!attribute.isEmpty())
                map.put(attribute.split(":")[0], Boolean.parseBoolean(attribute.split(":")[1]));
        return map;
    }

    /**
     * Checks if any module uses GUI
     *
     * @return
     */
    public static void calculateModulesUseGui() {
        FarmerAPI.getModuleManager().setModulesUseGui(FarmerAPI.getModuleManager().getModuleList().stream().anyMatch(FarmerModule::isHasGui));
    }
}