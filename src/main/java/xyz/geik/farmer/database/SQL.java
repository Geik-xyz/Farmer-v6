package xyz.geik.farmer.database;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.handlers.FarmerBoughtEvent;
import xyz.geik.farmer.api.handlers.FarmerRemoveEvent;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.model.inventory.FarmerInv;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.farmer.model.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * SQL Queries and abstracted methods exist in this class
 *
 * @since b000
 * @author Amowny
 */
public abstract class SQL {

    /**
     * Instance of main
     */
    protected Main plugin = Main.getInstance();

    /**
     * Library which used for connection and pooling queries
     */
    protected HikariCP hikariCP;

    /**
     * Creates table on db
     */
    public abstract void createTable();

    /**
     * Updates all farmers which saves all data of farmers on cache
     */
    public void updateAllFarmers() {
        Connection connection = null;
        try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            for (Farmer farmer : FarmerManager.getFarmers().values()) {
                farmer.saveFarmer();
                FarmerAPI.getModuleManager().databaseUpdateAttribute(connection, farmer);
            }
        } catch (SQLException throwables) {
            this.plugin.getLogger().info("Error while updating Farmers: " + throwables.getMessage());
        } finally {
            closeConnections(null, connection, null);
        }
    }

    /**
     * Does same thing with #updateAllFarmers async
     */
    public void updateAllFarmersAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), this::updateAllFarmers);
    }

    /**
     * Loads all farmer data from sql to cache
     */
    public void loadAllFarmers() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // Query of farmer
        final String FARMER_QUERY = "SELECT * FROM Farmers;";
        // Query of farmer users
        final String USERS_QUERY = "SELECT * FROM FarmerUsers WHERE farmerId = ?";
        try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            preparedStatement = connection.prepareStatement(FARMER_QUERY);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int farmerID = resultSet.getInt("id");
                String regionID = resultSet.getString("regionID");
                int state = resultSet.getInt("state");
                int levelID = resultSet.getInt("level");

                if (levelID < 0)
                    levelID = 0;
                FarmerLevel level = (FarmerLevel.getAllLevels().size()-1 >= levelID )
                        ? FarmerLevel.getAllLevels().get(levelID)
                        : FarmerLevel.getAllLevels().get(FarmerLevel.getAllLevels().size()-1);

                List<FarmerItem> items = FarmerItem.deserializeItems(resultSet.getString("items"));

                FarmerInv inv = new FarmerInv(items, level.getCapacity());

                preparedStatement = connection.prepareStatement(USERS_QUERY);
                preparedStatement.setInt(1, farmerID);
                resultSet = preparedStatement.executeQuery();
                Set<User> users = new LinkedHashSet<>();

                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String uuid = resultSet.getString("uuid");

                    FarmerPerm role = FarmerPerm.getRole(resultSet.getInt("role"));
                    users.add(new User(farmerID, name, UUID.fromString(uuid), role));
                }
                Farmer farmer = new Farmer(farmerID, regionID, users, inv, level, state);
                FarmerAPI.getModuleManager().databaseGetAttributes(connection, farmer);
                FarmerManager.getFarmers().put(regionID, farmer);
            }
        } catch (SQLException throwables) {
            this.plugin.getLogger().info("Error while loading Farmers: " + throwables.getMessage());
        } finally {
            closeConnections(preparedStatement, connection, resultSet);
        }
    }

    /**
     * Creates farmer on sql
     * @param farmer temp farmer
     */
    public void createFarmer(@NotNull Farmer farmer) {
        Connection connection = null;
        PreparedStatement saveStatement = null;
        PreparedStatement selectStatement = null;
        final String SQL_QUERY = "INSERT INTO Farmers (regionID, state, level) VALUES (?, ?, ?)";
        try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            saveStatement = connection.prepareStatement(SQL_QUERY);
            saveStatement.setString(1, farmer.getRegionID());
            saveStatement.setInt(2, farmer.getState());
            saveStatement.setInt(3, FarmerLevel.getAllLevels().indexOf(farmer.getLevel()));
            saveStatement.executeUpdate();

            selectStatement = connection.prepareStatement("SELECT id FROM Farmers WHERE regionID = ?");
            selectStatement.setString(1, farmer.getRegionID());
            int id = selectStatement.executeQuery().getInt("id");

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                farmer.setId(id);
                FarmerBoughtEvent boughtEvent = new FarmerBoughtEvent(farmer);
                Bukkit.getPluginManager().callEvent(boughtEvent);
            });

        } catch (SQLException throwables) {
            this.plugin.getLogger().info("Error while creating Farmer: " + throwables.getMessage());
        } finally {
            closeConnections(saveStatement, connection, null);
            closeConnections(selectStatement, connection, null);
        }
    }

    /**
     * Removes farmer from sql
     * @param farmer object of farmer
     */
    public void removeFarmer(@NotNull Farmer farmer) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String DELETE_FARMER = "DELETE FROM Farmers WHERE id = ?";
        String DELETE_USERS = "DELETE FROM FarmerUsers WHERE farmerId = ?";
        try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            preparedStatement = connection.prepareStatement(DELETE_FARMER);
            preparedStatement.setInt(1, farmer.getId());
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(DELETE_USERS);
            preparedStatement.setInt(1, farmer.getId());
            preparedStatement.executeUpdate();

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                FarmerRemoveEvent removeEvent = new FarmerRemoveEvent(farmer);
                Bukkit.getPluginManager().callEvent(removeEvent);
            });

            if (FarmerManager.getFarmers().containsKey(farmer.getRegionID()))
                FarmerManager.getFarmers().remove(farmer.getRegionID());

        } catch (SQLException throwables) {
            this.plugin.getLogger().info("Error while removing Farmer: " + throwables.getMessage());
        } finally {
            closeConnections(preparedStatement, connection, null);
        }
    }

    /**
     * Saves farmer sync
     * @param connection connection object
     */
    public void saveFarmer(@NotNull Farmer farmer) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        final String SQL_QUERY = "UPDATE Farmers SET regionID = ?, state = ?, items = ?, level = ? WHERE id = ?";
        try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            preparedStatement = connection.prepareStatement(SQL_QUERY);
            preparedStatement.setString(1, farmer.getRegionID());
            preparedStatement.setInt(2, farmer.getState());
            String serializedItems = FarmerItem.serializeItems(farmer.getInv().getItems());
            preparedStatement.setString(3, (serializedItems == "") ? null : serializedItems);
            preparedStatement.setInt(4, FarmerLevel.getAllLevels().indexOf(farmer.getLevel()));
            preparedStatement.setInt(5, farmer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            this.plugin.getLogger().info("Error while save Farmer: " + throwables.getMessage());
        } finally {
            closeConnections(preparedStatement, connection, null);
        }
    }

    /**
     * Adds user to farmer in sql
     *
     * @param uuid uuid of player
     * @param name name of player
     * @param perm perm of player
     */
    public void addUser(UUID uuid, String name, FarmerPerm perm, @NotNull Farmer farmer) {
        farmer.getUsers().add(new User(farmer.getId(), name, uuid, perm));
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            final String SQL_QUERY = "INSERT INTO FarmerUsers (farmerId, name, uuid, role) VALUES (?, ?, ?, ?)";
            try {
                connection = this.hikariCP.getHikariDataSource().getConnection();
                preparedStatement = connection.prepareStatement(SQL_QUERY);
                preparedStatement.setInt(1, farmer.getId());
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, uuid.toString());
                preparedStatement.setInt(4, FarmerPerm.getRoleId(perm));
                preparedStatement.executeUpdate();
            } catch (SQLException throwables) {
                this.plugin.getLogger().info("Error while adding User: " + throwables.getMessage());
            } finally {
                closeConnections(preparedStatement, connection, null);
            }
        });
    }

    /**
     * Removes user from farmer in sql
     *
     * @param user user object
     * @return status of task
     */
    public boolean removeUser(@NotNull User user, Farmer farmer) {
        if (user.getPerm().equals(FarmerPerm.OWNER))
            return false;
        farmer.getUsers().remove(user);
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            final String QUERY = "DELETE FROM FarmerUsers WHERE uuid = ? AND farmerId = ?";
            try {
                connection = this.hikariCP.getHikariDataSource().getConnection();
                preparedStatement = connection.prepareStatement(QUERY);
                preparedStatement.setString(1, user.getUuid().toString());
                preparedStatement.setInt(2, farmer.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException throwables) {
                this.plugin.getLogger().info("Error while remove User: " + throwables.getMessage());
            } finally {
                closeConnections(preparedStatement, connection, null);
            }
        });
        return true;
    }

    /**
     * Updates role of player
     * @param uuid uuid of player
     * @param roleId roleId of player (FarmerPerm#getRole)
     * @param farmerId id of farmer
     */
    public void updateRole(UUID uuid, int roleId, int farmerId) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            final String QUERY = "UPDATE FarmerUsers SET role = ? WHERE uuid = ? AND farmerId = ?";
            try {
                connection = this.hikariCP.getHikariDataSource().getConnection();
                preparedStatement = connection.prepareStatement(QUERY);
                preparedStatement.setInt(1, roleId);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.setInt(3, farmerId);
                preparedStatement.executeUpdate();
            } catch (SQLException throwables) {
                this.plugin.getLogger().info("Error while remove User: " + throwables.getMessage());
            } finally {
                closeConnections(preparedStatement, connection, null);
            }
        });
    }

    /**
     * Database type of sql
     * @return DatabaseType object
     */
    public abstract DatabaseType getDatabaseType();

    /**
     * Closes connection of sql
     *
     * @param preparedStatement statement
     * @param connection connection
     * @param resultSet resultset
     */
    protected void closeConnections(PreparedStatement preparedStatement, Connection connection, ResultSet resultSet) {
        if (connection == null)
            return;
        try {
            if (connection.isClosed())
                return;
            if (resultSet != null)
                resultSet.close();
            if (preparedStatement != null)
                preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}