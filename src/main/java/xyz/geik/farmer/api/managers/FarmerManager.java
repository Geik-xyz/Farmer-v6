package xyz.geik.farmer.api.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.database.DBQueries;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.farmer.model.user.User;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * Farmer Manager class manages all farmer related methods.
 */
public class FarmerManager {

    /**
     * Loaded farmer cache.
     */
    private HashMap<String, Farmer> farmers = new HashMap<>();
    public HashMap<String, Farmer> getFarmers() { return farmers; }

    /**
     * **DANGER**
     * Remove farmer from database and cache.
     * Cannot be UNDONE.
     *
     * @param regionId
     * @return
     */
    public boolean removeFarmer(String regionId) {
        if (FarmerAPI.getFarmerManager().getFarmers().containsKey(regionId)) {
            DBQueries.removeFarmer(FarmerAPI.getFarmerManager().getFarmers().get(regionId));
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
    public void changeOwner(UUID oldOwner, UUID newOwner, String regionId) {
        if (FarmerAPI.getFarmerManager().getFarmers().containsKey(regionId)) {
            Farmer toUpdate = FarmerAPI.getFarmerManager().getFarmers().get(regionId);
            // Adds player if not exists on farmer users
            if (toUpdate.getUsers().stream().noneMatch(user -> user.getUuid().equals(newOwner)))
                toUpdate.getUsers().add(new User(toUpdate.getId(), Bukkit.getOfflinePlayer(newOwner).getName(), newOwner, FarmerPerm.OWNER));
            // Update role on cache
            toUpdate.getUsers().stream().forEach(user -> {
                if (user.getUuid().equals(oldOwner))
                    user.setPerm(FarmerPerm.COOP);
                else if (user.getUuid().equals(newOwner) && !user.getPerm().equals(FarmerPerm.OWNER))
                    user.setPerm(FarmerPerm.OWNER);
            });
            // Update role on database
            int farmerId = toUpdate.getId();
            User.updateRole(oldOwner, 1, farmerId);
            User.updateRole(newOwner, 2, farmerId);
            // Update farmer regionId if same as ownerid
            if (regionId.equals(oldOwner.toString())) {
                toUpdate.setRegionID(newOwner.toString());
                FarmerAPI.getFarmerManager().getFarmers().put(newOwner.toString(), toUpdate);
                FarmerAPI.getFarmerManager().getFarmers().remove(regionId);
                toUpdate.saveFarmerAsync();
            }
        }
    }

    /**
     * Checks if farmer exists on location.
     *
     * @param location
     * @return
     */
    public boolean hasFarmer(Location location) {
        return FarmerAPI.getFarmerManager().getFarmers().keySet().contains(Main.getIntegration().getRegionID(location));
    }

    /**
     * Finds user list of farmer from region id.
     *
     * @param farmerId
     * @return
     */
    public Set<User> getUsers(String farmerId) {
        return FarmerAPI.getFarmerManager().getFarmers().get(farmerId).getUsers();
    }

    /**
     * Finds user list of farmer from location.
     *
     * @param location
     * @return
     */
    public Set<User> getUsers(Location location) {
        return FarmerAPI.getFarmerManager().getFarmers().get(Main.getIntegration().getRegionID(location)).getUsers();
    }
}
