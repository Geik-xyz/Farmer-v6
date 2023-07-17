package xyz.geik.farmer.database;

import org.bukkit.Bukkit;
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

public class MySQL extends SQL{
    private final String farmers_table;
    private final String farmer_users_table;

    public MySQL() {
        String hostname = Main.getDatabaseFile().getString("database.host");
        int port = Main.getDatabaseFile().getInt("database.port");
        String username = Main.getDatabaseFile().getString("database.username");
        String password = Main.getDatabaseFile().getString("database.password");
        String database = Main.getDatabaseFile().getString("database.database");
        this.farmers_table = Main.getDatabaseFile().getString("database.farmers_table");
        this.farmer_users_table = Main.getDatabaseFile().getString("database.farmer_users_table");
        this.hikariCP = new HikariCP(hostname, port, username, password, database);
        this.hikariCP.setProperties(this);
        createTable();
    }

    public void createTable() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + this.farmers_table + " (id INT AUTO_INCREMENT, regionID varchar(36) NOT NULL UNIQUE, `state` smallint(1) DEFAULT 1, `items` text DEFAULT NULL, `attributes` text DEFAULT NULL, `level` int DEFAULT 0, PRIMARY KEY (id))");
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + this.farmer_users_table + " (`farmerId` int NOT NULL, `name` varchar(30) DEFAULT 'User', `uuid` char(36) DEFAULT '0', `role` smallint(1) DEFAULT 0)");
            preparedStatement.executeUpdate();
            this.plugin.getLogger().info("MySQL tables created successfully!");
        } catch (SQLException throwables) {
            this.plugin.getLogger().info("Error while creating table: " + throwables.getMessage());
        } finally {
            closeConnections(preparedStatement, connection, null);
        }
    }

    public void updateAllFarmers() {
        Connection connection = null;
        try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            for (Farmer farmer : FarmerManager.getFarmers().values()) {
                Main.getInstance().getSql().saveFarmer(connection);
                FarmerAPI.getModuleManager().databaseUpdateAttribute(connection, farmer);
            }
        } catch (SQLException throwables) {
            this.plugin.getLogger().info("Error while updating Farmers: " + throwables.getMessage());
        } finally {
            closeConnections(null, connection, null);
        }
    }

    public void updateAllFarmersAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), this::updateAllFarmers);
    }

    public void loadAllFarmers() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // Query of farmer
        final String FARMER_QUERY = "SELECT * FROM " + this.farmers_table;
        // Query of farmer users
        final String USERS_QUERY = "SELECT * FROM " + this.farmer_users_table + " WHERE farmerId = ?";
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

    public void createFarmer(Farmer farmer) {
        Connection connection = null;
        PreparedStatement insertStatement = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            insertStatement = connection.prepareStatement("INSERT INTO " + this.farmers_table + " (regionID, state, level) VALUES (?, ?, ?)");
            selectStatement = connection.prepareStatement("SELECT id FROM " + this.farmers_table + " WHERE regionID = ?");

            insertStatement.setString(1, farmer.getRegionID());
            insertStatement.setInt(2, farmer.getState());
            insertStatement.setInt(3, FarmerLevel.getAllLevels().indexOf(farmer.getLevel()));
            insertStatement.executeUpdate();

            selectStatement.setString(1, farmer.getRegionID());
            resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    farmer.setId(id);
                    FarmerBoughtEvent boughtEvent = new FarmerBoughtEvent(farmer);
                    Bukkit.getPluginManager().callEvent(boughtEvent);
                });
            }
        } catch (SQLException throwables) {
            this.plugin.getLogger().info("Error while creating Farmer: " + throwables.getMessage());
        } finally {
            closeConnections(null, connection, resultSet);
        }
    }

    public void removeFarmer(Farmer farmer) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String DELETE_FARMER = "DELETE FROM " + this.farmers_table + " WHERE id = ?";
        String DELETE_USERS = "DELETE FROM " + this.farmer_users_table + " WHERE farmerId = ?";
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

    public void saveFarmerAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Connection connection = null;
            try {
                connection = this.hikariCP.getHikariDataSource().getConnection();
                saveFarmer(connection);
            } catch (SQLException throwables) {
                this.plugin.getLogger().info("Error while saving Farmer: " + throwables.getMessage());
            }
        });
    }

    public void saveFarmer(Connection connection) {
        connection = null;
        PreparedStatement preparedStatement = null;
        final String SQL_QUERY = "UPDATE " + this.farmers_table + " SET regionID = ?, state = ?, items = ?, level = ? WHERE id = ?";
        try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            preparedStatement = connection.prepareStatement(SQL_QUERY);
            for (Farmer farmer : FarmerManager.getFarmers().values()) {
                preparedStatement.setString(1, farmer.getRegionID());
                preparedStatement.setInt(2, farmer.getState());
                String serializedItems = FarmerItem.serializeItems(farmer.getInv().getItems());
                preparedStatement.setString(3, (serializedItems == "") ? null : serializedItems);
                preparedStatement.setInt(4, FarmerLevel.getAllLevels().indexOf(farmer.getLevel()));
                preparedStatement.setInt(5, farmer.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException throwables) {
            this.plugin.getLogger().info("Error while save Farmer: " + throwables.getMessage());
        } finally {
            closeConnections(preparedStatement, connection, null);
        }
    }

    public void addUser(UUID uuid, String name, FarmerPerm perm) {
        for (Farmer farmer : FarmerManager.getFarmers().values()) {
            farmer.getUsers().add(new User(farmer.getId(), name, uuid, perm));
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                final String SQL_QUERY = "INSERT INTO " + this.farmer_users_table + " (farmerId, name, uuid, role) VALUES (?, ?, ?, ?)";
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
    }

    public boolean removeUser(User user) {
        if (user.getPerm().equals(FarmerPerm.OWNER))
            return false;
        for (Farmer farmer : FarmerManager.getFarmers().values()) {
            farmer.getUsers().remove(user);
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                final String QUERY = "DELETE FROM " + this.farmer_users_table + " WHERE uuid = ? AND farmerId = ?";
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
        }
        return true;
    }

    public void updateRole(UUID uuid, int roleId, int farmerId) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            final String QUERY = "UPDATE " + this.farmer_users_table + " SET role = ? WHERE uuid = ? AND farmerId = ?";
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

    public DatabaseType getDatabaseType() {
        return DatabaseType.MYSQL;
    }
}
