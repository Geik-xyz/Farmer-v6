package xyz.geik.farmer.api;

import org.bukkit.Location;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.database.DBConnection;
import xyz.geik.farmer.database.DBQueries;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.user.User;

import java.sql.Connection;
import java.util.Set;
import java.util.UUID;

/**
 * If something necessary i will write it to here.
 * This is main api which usable for easy manipulations.
 */
public class FarmerAPI {

    public static Main getInstance() {
        return Main.getInstance();
    }

    /**
     * **DANGER**
     * Remove farmer from database and cache.
     * Cannot be UNDONE.
     *
     * @param regionId
     * @return
     */
    public static boolean removeFarmer(String regionId) {
        if (FarmerAPI.getInstance().getFarmers().containsKey(regionId)) {
            DBQueries.removeFarmer(Main.getFarmers().get(regionId));
            return true;
        }
        return false;
    }

    /**
     * Changes owner of farmer.
     *
     * @param oldOwner
     * @param newOwner
     * @param regionId
     */
    public static void changeOwner(UUID oldOwner, UUID newOwner, String regionId) {
        if (FarmerAPI.getInstance().getFarmers().containsKey(regionId)) {
            int farmerId = Main.getFarmers().get(regionId).getId();
            User.updateRole(oldOwner, 0, farmerId);
            User.updateRole(newOwner, 2, farmerId);
            if (regionId.equals(oldOwner.toString())) {
                Farmer farmer = Main.getFarmers().get(regionId);
                farmer.setRegionID(newOwner.toString());
                Main.getFarmers().put(newOwner.toString(), farmer);
                farmer.saveFarmer();
                FarmerAPI.removeFarmer(regionId);
            }
        }
    }

    /**
     * Checks if farmer exists on location.
     *
     * @param location
     * @return
     */
    public static boolean hasFarmer(Location location) {
        return Main.getFarmers().keySet().contains(Main.getIntegration().getRegionID(location));
    }

    /**
     * Connects database of farmer.
     *
     * @return
     */
    public static Connection connectDatabase() {
        return DBConnection.connect();
    }

    /**
     * Finds user list of farmer from region id.
     *
     * @param farmerId
     * @return
     */
    public Set<User> getUsers(String farmerId) {
        return Main.getFarmers().get(farmerId).getUsers();
    }

    /**
     * Finds user list of farmer from location.
     *
     * @param location
     * @return
     */
    public Set<User> getUsers(Location location) {
        return Main.getFarmers().get(Main.getIntegration().getRegionID(location)).getUsers();
    }
}
