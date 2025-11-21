package dev.team.hytalib.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcDatabaseImpl implements JdbcDatabase {

    private final HikariDataSource dataSource;

    public JdbcDatabaseImpl(String jdbcUrl, Properties props, DatabaseBuilder.PoolSettings pool) {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(jdbcUrl);

        // Apply login credentials if available
        if (props.containsKey("user")) config.setUsername(props.getProperty("user"));
        if (props.containsKey("password")) config.setPassword(props.getProperty("password"));

        props.forEach((key, value) -> {
            if (!key.equals("user") && !key.equals("password"))
                config.addDataSourceProperty(key.toString(), value);
        });

        // --- Connection Pool Settings ---
        config.setMaximumPoolSize(pool.maxPoolSize);
        config.setMinimumIdle(pool.minIdle);
        config.setConnectionTimeout(pool.connectionTimeout);
        config.setIdleTimeout(pool.idleTimeout);
        config.setMaxLifetime(pool.maxLifetime);
        config.setPoolName("HytalibPool");

        this.dataSource = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get database connection from HikariCP", e);
        }
    }

    @Override
    public void close() {
        dataSource.close();
    }
}
