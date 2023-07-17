package xyz.geik.farmer.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCP {
    private HikariDataSource hikariDataSource;
    protected String hostname, database, username, password;
    protected int port;

    public HikariCP(String hostname, int port, String username, String password, String database) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    public void setProperties(SQL sql) {
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

    public HikariCP() {}

    public HikariDataSource getHikariDataSource() {
        return this.hikariDataSource;
    }

}
