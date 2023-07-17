package xyz.geik.farmer.database;

import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.farmer.model.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public abstract class SQL {

    protected Main plugin = Main.getInstance();
    protected HikariCP hikariCP;

    public abstract void createTable();
    public abstract void updateAllFarmers();
    public abstract void updateAllFarmersAsync();
    public abstract void loadAllFarmers();
    public abstract void createFarmer(Farmer farmer);
    public abstract void removeFarmer(Farmer farmer);
    public abstract void saveFarmerAsync();
    public abstract void saveFarmer(Connection connection);
    public abstract void addUser(UUID uuid, String name, FarmerPerm perm);
    public abstract boolean removeUser(User user);
    public abstract void updateRole(UUID uuid, int roleId, int farmerId);
    public abstract DatabaseType getDatabaseType();

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
