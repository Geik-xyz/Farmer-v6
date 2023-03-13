package xyz.geik.farmer.api.managers;

import xyz.geik.farmer.database.DBConnection;

import java.sql.Connection;

public class DatabaseManager {

    /**
     * Connects database of farmer.
     *
     * @return
     */
    public static Connection connectDatabase() {
        return DBConnection.connect();
    }

}
