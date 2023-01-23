package xyz.geik.farmer.api;

import org.bukkit.Location;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.database.DBConnection;
import xyz.geik.farmer.database.DBQueries;
import xyz.geik.farmer.model.user.User;

import java.sql.Connection;
import java.util.Set;

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
     * @param farmerId
     * @return
     */
    public static boolean removeFarmer(String farmerId) {
        if (FarmerAPI.getInstance().getFarmers().containsKey(farmerId)) {
            DBQueries.removeFarmer(Main.getFarmers().get(farmerId));
            FarmerAPI.removeFarmer(farmerId);
            return true;
        }
        return false;
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
