package org.collexio;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static boolean initIfNeeded(Connection connection) throws SQLException, IOException {
        if (!isInitialized(connection)) {
            runSchema(connection);
            return true;
        }
        return false;
    }

    private static boolean isInitialized(Connection connection) throws SQLException {
        var meta = connection.getMetaData();
        try (var rs = meta.getTables(null, null, "item_collections", null)) {
            return rs.next();
        }
    }

    private static void runSchema(Connection connection) throws SQLException, IOException {
        try (InputStream in = DatabaseInitializer.class
                .getResourceAsStream("/schema.sql")) {
            if (in == null) throw new IOException("schema.sql not founded");
            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            try (Statement stmt = connection.createStatement()) {
                for (String s : sql.split(";")) {
                    String trimmed = s.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
            }
        }
    }
}