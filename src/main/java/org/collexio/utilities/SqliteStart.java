package org.collexio.utilities;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteStart {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:my.db";
        String seedFile = "data/seed.sql";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                var meta = conn.getMetaData();
                System.out.println("Driver name: " + meta.getDriverName());
                System.out.println("Database created or opened.");
                String sql = new String(Files.readAllBytes(Paths.get(seedFile)));
                try (Statement stmt = conn.createStatement()) {
                    for (String sqlStatement : sql.split(";")) {
                        sqlStatement = sqlStatement.trim();
                        if (!sqlStatement.isEmpty()) {
                            stmt.execute(sqlStatement);
                            System.out.println("Executed: " + sqlStatement);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
