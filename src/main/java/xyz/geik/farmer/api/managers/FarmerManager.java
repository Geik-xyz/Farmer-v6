package xyz.geik.farmer.api.managers;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.farmer.model.user.User;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * Farmer Manager class manages all farmer related methods.
 * @author poyrazinan
 */
public class FarmerManager {

    /**
     * Constructor of class
     */
    public FarmerManager() {}

    /**
     * Loaded farmer cache.
     * @see Farmer
     */
    @Getter
    public static HashMap<String, Farmer> farmers = new HashMap<>();

    /**
     * <b>DANGER</b> - Use with caution.
     * <p>
     *     Remove farmer from database and cache.
     *     Cannot be UNDONE.
     * </p>
     *
     * @param regionId Region id of farmer
     * @return true if farmer was removed, false if not
     */
    public boolean removeFarmer(String regionId) {
        if (getFarmers().containsKey(regionId)) {
            Main.getInstance().getSql().removeFarmer(getFarmers().get(regionId));
            return true;
        }
        return false;
    }

    /**
     * Changes owner of farmer.
     *
     * @param oldOwner Old owner of farmer
     * @param newOwner New owner of farmer
     * @param regionId Region id of farmer
     */
    @SneakyThrows
    public void changeOwner(UUID oldOwner, UUID newOwner, String regionId) {
        if (getFarmers().containsKey(regionId)) {
            Farmer farmer = getFarmers().get(regionId);
            Farmer newFarmer = farmer.clone();
            getFarmers().remove(regionId);
            FarmerAPI.getFarmerManager().removeFarmer(regionId);
            // Replaces old owner role to coop on db
            Main.getInstance().getSql().updateRole(oldOwner, 1, newFarmer.getId());
            // Replace old owner role to coop on cache
            newFarmer.getUsers().stream().filter(user -> user.getUuid().equals(oldOwner)).findFirst().get().setPerm(FarmerPerm.MEMBER);
            // Adds player if not exists on farmer users
            if (newFarmer.getUsers().stream().noneMatch(user -> user.getUuid().equals(newOwner)))
                /*
                 * Changed to use farmer's
                 * addUser method instead of
                 * adding into array.
                 *
                 * Because adding array not saving
                 * into database on new players
                 * and removes farmer on restart.
                 *
                 * @author WaterArchery
                 */
                farmer.addUser(newOwner, Bukkit.getOfflinePlayer(newOwner).getName(), FarmerPerm.OWNER);
            else {
                // Replaces new owner role to owner on db
                Main.getInstance().getSql().updateRole(newOwner, 2, newFarmer.getId());
                // Replaces new owner role to owner on cache
                newFarmer.getUsers().stream().filter(user -> user.getUuid().equals(newOwner)).findFirst().get().setPerm(FarmerPerm.OWNER);
            }
            // Update farmer regionId if same as ownerid
            if (regionId.equals(oldOwner.toString()))
                newFarmer.setRegionID(newOwner.toString());
            getFarmers().put(newFarmer.getRegionID(), newFarmer);
            // Saves farmer to db
            farmer.saveFarmer();
        }
    }

    /**
     * Fresh creates farmer
     *
     * @param regionId id of region
     * @param level level of farmer
     */
    public Farmer createFarmer(String regionId, int level) {
        Farmer farmer = new Farmer(regionId, 0);
        farmer.setLevel(FarmerLevel.getAllLevels().get(level-1));
        return farmer;
    }

    /**
     * Checks if farmer exists on location.
     *
     * @param location Location to check
     * @return true if farmer exists, false if not
     */
    public boolean hasFarmer(Location location) {
        return getFarmers().containsKey(Main.getIntegration().getRegionID(location));
    }

    /**
     * Finds user list of farmer from region id.
     *
     * @param regionId region id of farmer
     * @return user set of farmer
     * @see User
     * @see Set
     */
    public Set<User> getUsers(String regionId) {
        return getFarmers().get(regionId).getUsers();
    }

    /**
     * Finds user list of farmer from location.
     *
     * @param location location of farmer region
     * @return user set of farmer
     * @see User
     * @see Set
     */
    public Set<User> getUsers(Location location) {
        return getFarmers().get(Main.getIntegration().getRegionID(location)).getUsers();
    }
}
