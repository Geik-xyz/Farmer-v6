package xyz.geik.farmer.database;

import lombok.Getter;
import xyz.geik.farmer.Main;
import xyz.geik.glib.database.DatabaseAPI;

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
@Getter
public class SQLite extends SQL {

    /**
     * Main constructor of SQLite
     */
    public SQLite() {
        Main.setSql(this);
        Main.setDatabase(new DatabaseAPI(Main.getInstance()).getDatabase());
        // Crates tables
        createTable();
    }

    /**
     * Create tables if there is no any
     */
    public void createTable() {
        String farmersTable = "CREATE TABLE IF NOT EXISTS Farmers (id INTEGER PRIMARY KEY AUTOINCREMENT, regionID varchar(36) NOT NULL UNIQUE, `state` smallint(1) DEFAULT 1, `items` text DEFAULT NULL, `attributes` text DEFAULT NULL, `level` int DEFAULT 0)";
        String usersTable = "CREATE TABLE IF NOT EXISTS FarmerUsers (farmerId int NOT NULL, `name` varchar(30) DEFAULT User, uuid char(36) DEFAULT 0, `role` smallint(1) DEFAULT 0)";
        Main.getDatabase().createTables(farmersTable);
        Main.getDatabase().createTables(usersTable);
    }

    /**
     * Returns type of database
     * @return DatabaseType enum #SQLITE / #MYSQL
     */
    public DatabaseType getDatabaseType() {
        return DatabaseType.SQLITE;
    }
}
