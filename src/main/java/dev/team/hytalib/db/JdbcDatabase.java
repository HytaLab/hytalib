package dev.team.hytalib.db;

import java.sql.Connection;

public interface JdbcDatabase extends Database {
    Connection getConnection();
}