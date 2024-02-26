package xyz.geik.farmer.database;

import lombok.Getter;
import xyz.geik.farmer.Main;
import xyz.geik.glib.database.DatabaseAPI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * MySQL class for database connection
 *
 * @since b000
 * @author Amowny
 */
@Getter
public class MySQL extends SQL {

    /**
     * Main constructor of MySQL init configurations
     */
    public MySQL() {
        String host = Main.getConfigFile().getDatabase().getHost();
        String port = Main.getConfigFile().getDatabase().getPort();
        String dbName = Main.getConfigFile().getDatabase().getTableName();
        String username = Main.getConfigFile().getDatabase().getUserName();
        String password = Main.getConfigFile().getDatabase().getPassword();
        Main.setDatabase(new DatabaseAPI(Main.getInstance(), host, port, dbName, username, password).getDatabase());
        createTable();
    }

    /**
     * Crates table of MySQL
     */
    public void createTable() {
        String farmersTable = "CREATE TABLE IF NOT EXISTS Farmers (id INT AUTO_INCREMENT, regionID varchar(36) NOT NULL UNIQUE, `state` smallint(1) DEFAULT 1, `items` text DEFAULT NULL, `attributes` text DEFAULT NULL, `level` int DEFAULT 0, PRIMARY KEY (id))";
        String usersTable = "CREATE TABLE IF NOT EXISTS FarmerUsers (`farmerId` int NOT NULL, `name` varchar(30) DEFAULT 'User', `uuid` char(36) DEFAULT '0', `role` smallint(1) DEFAULT 0)";
        Main.getDatabase().createTables(farmersTable);
        Main.getDatabase().createTables(usersTable);
    }

    /**
     * Gets type of database
     * @return DatabaseType#MYSQL
     */
    public DatabaseType getDatabaseType() {
        return DatabaseType.MYSQL;
    }
}
