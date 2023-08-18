package xyz.geik.farmer.database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SQLite query class
 *
 * @since b000
 * @author Amowny
 */
public class SQLite extends SQL {

    /**
     * SQLite file
     */
    private final File sqlFile;

    /**
     * Main constructor of SQLite
     */
    public SQLite() {
        // HikariCP library object
        this.hikariCP = new HikariCP();
        // File of sqlite
        this.sqlFile = new File(this.plugin.getDataFolder() + "/storage/", "database.db");
        // Configuration setter of hikaricp
        this.hikariCP.setProperties(this);
        // Crates tables
        createTable();
    }

    /**
     * Create tables if there is no any
     */
    public void createTable() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.hikariCP.getHikariDataSource().getConnection();
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Farmers (id INTEGER PRIMARY KEY AUTOINCREMENT, regionID varchar(36) NOT NULL UNIQUE, `state` smallint(1) DEFAULT 1, `items` text DEFAULT NULL, `attributes` text DEFAULT NULL, `level` int DEFAULT 0)");
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS FarmerUsers (farmerId int NOT NULL, `name` varchar(30) DEFAULT User, uuid char(36) DEFAULT 0, `role` smallint(1) DEFAULT 0)");
            preparedStatement.executeUpdate();
            this.plugin.getLogger().info("SQLite tables created successfully");
        } catch (SQLException throwables) {
            this.plugin.getLogger().info("Error while creating table: " + throwables.getMessage());
        } finally {
            closeConnections(preparedStatement, connection, null);
        }
    }

    /**
     * Returns type of database
     * @return DatabaseType enum #SQLITE / #MYSQL
     */
    public DatabaseType getDatabaseType() {
        return DatabaseType.SQLITE;
    }

    /**
     * Gets file of SQLite
     * @return File of .db file
     */
    public File getSqlFile() {
        return this.sqlFile;
    }
}
