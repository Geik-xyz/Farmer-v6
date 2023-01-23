package xyz.geik.farmer.database;

import org.jetbrains.annotations.Nullable;
import xyz.geik.farmer.Main;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Creates basic connection to SQLite database.
 */
public class DBConnection {

    /**
     * Connects database of Farmer.
     *
     * @return
     */
    public static @Nullable Connection connect() {
        try {
            // JDBC Class for SQLite
            Class.forName("org.sqlite.JDBC");
            // Getting plugin name for folder destination
            String pluginName = Main.getInstance().getDescription().getName();
            // Getting file
            File databaseFile = new File("plugins/" + pluginName + "/storage/database.db");
            // Going in
            databaseFile.getParentFile().mkdirs();
            // Catching absolute path
            String absolutePath = databaseFile.getParentFile().getAbsolutePath();
            // Then using driver and return it.
            return DriverManager.getConnection("jdbc:sqlite:" + absolutePath + "/database.db");
        }
        catch (Exception e) { return null; }
    }

}
