package xyz.geik.farmer.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.database.DBConnection;
import xyz.geik.farmer.database.DBQueries;
import xyz.geik.farmer.model.inventory.FarmerInv;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.farmer.model.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Setter
@Getter
public class Farmer {

    private String regionID;
    private Set<User> users;
    private FarmerInv inv;
    private FarmerLevel level;
    private int state, id;

    public Farmer(int id, String regionID, Set<User> users,
                  FarmerInv inv, FarmerLevel level, int state) {
        this.id = id;
        this.regionID = regionID;
        this.users = users;
        this.inv = inv;
        this.level = level;
        this.state = state;
    }

    public Farmer(String regionID, UUID ownerUUID) {
        this.regionID = regionID;
        Set<User> users = new LinkedHashSet<>();
        this.users = users;
        this.inv = new FarmerInv();
        this.level = FarmerLevel.getAllLevels().get(0);
        this.state = 1;
        Main.getFarmers().put(regionID, this);
        DBQueries.createFarmer(this, ownerUUID);
    }

    public UUID getOwnerUUID() {
        return users.stream().filter(this::isUserOwner).findFirst().get().getUuid();
    }
    private boolean isUserOwner(User user) {
        return user.getPerm().equals(FarmerPerm.OWNER);
    }
    public Set<User> getUsersWithoutOwner() {
        return users.stream().filter(this::isUserNotOwner).collect(Collectors.toSet());
    }
    private boolean isUserNotOwner(User user) {
        return user.getPerm().equals(FarmerPerm.OWNER);
    }

    public void saveFarmer() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            // TODO Save farmer to database

        });
    }

    public void addUser(UUID uuid, String name) {
        addUser(uuid, name, 0);
    }

    public void addUser(UUID uuid, String name, int perm) {
        this.getUsers().add(new User(this.getId(), name, uuid, FarmerPerm.getRole(perm)));
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            final String QUERY = "INSERT INTO FarmerUsers (farmerId, name, uuid, role) VALUES (?, ?, ?, ?)";
            try (Connection con = DBConnection.connect()) {
                PreparedStatement statement = con.prepareStatement(QUERY);
                statement.setInt(1, this.getId());
                statement.setString(2, name);
                statement.setString(3, uuid.toString());
                statement.setInt(4, perm);
                statement.executeUpdate();
                statement.close();
            }
            catch (Exception e) { e.printStackTrace(); }
        });
    }

    public boolean removeUser(User user) {
        if (user.getPerm().equals(FarmerPerm.OWNER))
            return false;
        this.getUsers().remove(user);
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            final String QUERY = "DELETE FROM FarmerUsers WHERE uuid = ? AND farmerId = ?";
            try (Connection con = DBConnection.connect()) {
                PreparedStatement statement = con.prepareStatement(QUERY);
                statement.setString(1, user.getUuid().toString());
                statement.setInt(2, this.getId());
                statement.executeUpdate();
                statement.close();
            }
            catch (Exception e) { e.printStackTrace(); }
        });
        return true;
    }
}
