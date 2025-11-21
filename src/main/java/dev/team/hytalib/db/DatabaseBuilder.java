package dev.team.hytalib.db;

import java.util.Properties;

/**
 * HytaLab Studio code @ 2025
 */
public class DatabaseBuilder {

    private DatabaseTypes type;

    private String host = "localhost";
    private int port = -1;
    private String database;
    private String filePath;
    private String user;
    private String password;

    private Properties properties = new Properties();
    private PoolSettings pool = new PoolSettings();

    public static DatabaseBuilder create() { return new DatabaseBuilder(); }

    public DatabaseBuilder type(DatabaseTypes type) { this.type = type; return this; }

    public DatabaseBuilder host(String host) { this.host = host; return this; }
    public DatabaseBuilder port(int port) { this.port = port; return this; }

    public DatabaseBuilder database(String database) { this.database = database; return this; }
    public DatabaseBuilder filePath(String filePath) { this.filePath = filePath; return this; }

    public DatabaseBuilder user(String user) { this.user = user; return this; }
    public DatabaseBuilder password(String password) { this.password = password; return this; }

    public DatabaseBuilder property(String key, String value) {
        this.properties.setProperty(key, value);
        return this;
    }

    // --- Pool Setting Builder Methods ---
    public DatabaseBuilder maxPoolSize(int size) { pool.maxPoolSize = size; return this; }
    public DatabaseBuilder minIdle(int idle) { pool.minIdle = idle; return this; }
    public DatabaseBuilder connectionTimeout(long ms) { pool.connectionTimeout = ms; return this; }
    public DatabaseBuilder idleTimeout(long ms) { pool.idleTimeout = ms; return this; }
    public DatabaseBuilder maxLifetime(long ms) { pool.maxLifetime = ms; return this; }

    public Database build() {
        if (type == null)
            throw new IllegalStateException("DatabaseType is required.");

        return switch (type) {
            case H2    -> buildH2();
            case MYSQL -> buildMySQL();
            case SQLITE -> buildSQLite();
            case REDIS -> buildRedis();
        };
    }

    // --- JDBC Builders ---
    private JdbcDatabase buildH2() {
        if (filePath == null)
            throw new IllegalStateException("H2 requires filePath.");

        String url = "jdbc:h2:" + filePath + ";AUTO_SERVER=TRUE";
        prepareProps();
        return new JdbcDatabaseImpl(url, properties, pool);
    }

    private JdbcDatabase buildMySQL() {
        if (database == null)
            throw new IllegalStateException("MySQL requires database name.");

        String url = "jdbc:mysql://" + host + ":" + (port > 0 ? port : 3306)
                + "/" + database + "?useSSL=false&autoReconnect=true";

        prepareProps();
        return new JdbcDatabaseImpl(url, properties, pool);
    }

    private JdbcDatabase buildSQLite() {
        if (filePath == null)
            throw new IllegalStateException("SQLite requires filePath.");

        String url = "jdbc:sqlite:" + filePath;
        prepareProps();
        return new JdbcDatabaseImpl(url, properties, pool);
    }

    private JdbcDatabase buildRedis() {
        // not a jdbc database but for temp impl.



        /// not subject to do redis --- all placeholder stuff.....
        if (filePath == null)
            throw new IllegalStateException("SQLite requires filePath.");

        String url = "jdbc:sqlite:" + filePath;
        prepareProps();
        return new JdbcDatabaseImpl(url, properties, pool);
    }

    private void prepareProps() {
        if (user != null) properties.setProperty("user", user);
        if (password != null) properties.setProperty("password", password);
    }


    // --- Pool Config Container ---
    public static class PoolSettings {
        public int maxPoolSize = 10;
        public int minIdle = 2;
        public long connectionTimeout = 30000;
        public long idleTimeout = 600000;
        public long maxLifetime = 1800000;
    }
}
