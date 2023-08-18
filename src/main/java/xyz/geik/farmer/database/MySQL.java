package xyz.geik.farmer.database;

import lombok.Getter;
import xyz.geik.farmer.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @since b000
 * @author Amowny
 */
@Getter
public class MySQL extends SQL {

    /**
     * Main constructor of MySQL init configurations
     */
    public MySQL() {
        String hostname = Main.getDatabaseFile().getString("database.host");
        int port = Main.getDatabaseFile().getInt("database.port");
        String username = Main.getDatabaseFile().getString("database.username");
        String password = Main.getDatabaseFile().getString("database.password");
        String database = Main.getDatabaseFile().getString("database.database");
        this.hikariCP = new HikariCP(hostname, port, username, password, database);
        this.hikariCP.setProperties(this);
        createTable();
    }

    /**
     * Crates table of MySQL
     */
    public void createTable() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Farmers (id INT AUTO_INCREMENT, regionID varchar(36) NOT NULL UNIQUE, `state` smallint(1) DEFAULT 1, `items` text DEFAULT NULL, `attributes` text DEFAULT NULL, `level` int DEFAULT 0, PRIMARY KEY (id))");
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS FarmerUsers (`farmerId` int NOT NULL, `name` varchar(30) DEFAULT 'User', `uuid` char(36) DEFAULT '0', `role` smallint(1) DEFAULT 0)");
            preparedStatement.executeUpdate();
            this.plugin.getLogger().info("MySQL tables created successfully!");
        } catch (SQLException throwable) {
            this.plugin.getLogger().info("Error while creating table: " + throwable.getMessage());
        } finally {
            closeConnections(preparedStatement, connection, null);
        }
    }

    /**
     * Gets type of database
     * @return DatabaseType#MYSQL
     */
    public DatabaseType getDatabaseType() {
        return DatabaseType.MYSQL;
    }
}
