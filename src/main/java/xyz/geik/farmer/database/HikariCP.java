package xyz.geik.farmer.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * HikariCP main configuration class
 * @since b000
 * @author Amowny
 */
@Getter
public class HikariCP {

    /**
     * DataSource object of HikariCP
     */
    private HikariDataSource hikariDataSource;

    /**
     * MySQL auth information
     */
    protected String hostname, database, username, password;

    /**
     * MySQL port for connection
     */
    protected int port;

    /**
     * Constructor of HikariCP for MySQL
     *
     * @param hostname of mysql
     * @param port of mysql
     * @param username of mysql
     * @param password of mysql
     * @param database of mysql
     */
    public HikariCP(String hostname, int port, String username, String password, String database) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    /**
     * Constructor of HikariCP for SQLite
     * Empty on SQLite because there is no auth protection.
     */
    public HikariCP() {}

    /**
     * Sets property of mysql/sqlite
     * @param sql of hikaricp
     */
    public void setProperties(@NotNull SQL sql) {
        HikariConfig hikariConfig = new HikariConfig();
        if (sql.getDatabaseType() == DatabaseType.MYSQL) {
            String jdbcUrl = "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database;
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setUsername(this.username);
            hikariConfig.setPassword(this.password);
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        } else {
            String jdbcUrl = "jdbc:sqlite:" + ((SQLite)sql).getSqlFile();
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setDriverClassName("org.sqlite.JDBC");
        }
        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }
}
