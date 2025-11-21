package dev.team.hytalib.db;

import java.sql.Connection;

/**
 * HytaLab Studio code @ 2025
 */
public interface JdbcDatabase extends Database {
    Connection getConnection();
}