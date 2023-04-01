package xyz.geik.farmer.api.managers;

import xyz.geik.farmer.database.DBConnection;

import java.sql.Connection;

/**
 * Database manager class which connects to database.
 * @author poyrazinan
 */
public class DatabaseManager {

    /**
     * Connects database of farmer.
     *
     * @return Connection to database
     * @see Connection
     */
    public static Connection connectDatabase() {
        return DBConnection.connect();
    }

}
