package org.collexio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlite:" + AppConfig.getDbPath().toAbsolutePath();
        Connection connection = DriverManager.getConnection(url);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }
}