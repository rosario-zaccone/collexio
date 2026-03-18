package org.collexio;

import org.collexio.persistence.dao.ConnectionFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DATA_FOLDER = "data";
    private static final String SEED_FILE = "data/seed.sql";

    public static void initializeDatabase() {
        try {
            Path dataPath = Path.of(DATA_FOLDER);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }

            try (Connection conn = ConnectionFactory.getConnection()) {
                String sql = Files.readString(Path.of(SEED_FILE));
                try (Statement stmt = conn.createStatement()) {
                    for (String s : sql.split(";")) {
                        String trimmed = s.trim();
                        if (!trimmed.isEmpty()) {
                            stmt.execute(trimmed);
                        }
                    }
                }
            }

        } catch (IOException | SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
        System.out.println("Database ready.");
    }
}