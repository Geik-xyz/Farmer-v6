package xyz.geik.farmer.api.managers;

import xyz.geik.farmer.Main;

import java.sql.Connection;

/**
 * Database manager class which connects to database.
 * @author poyrazinan
 */
public class DatabaseManager {

    // MAYBE NOT WORK WITH NEW SYSTEM

    /**
     * Connects database of farmer.
     *
     * @return Connection to database
     * @see Connection
     */
    public static Connection connectDatabase() {
        return (Connection) Main.getInstance().getSql();
    }

}