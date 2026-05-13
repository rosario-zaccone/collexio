package org.collexio;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AppConfig {

    private static final String APP_DIR = ".collexio";

    public static Path getAppHome() {
        return Path.of(System.getProperty("user.home"), APP_DIR);
    }

    public static Path getDbPath() {
        return getAppHome().resolve("data/collexio.db");
    }

    public static Path getImagesDir() {
        return getAppHome().resolve("images");
    }

    public static Path getConfigFile() {
        return getAppHome().resolve("config.properties");
    }

    public static void initDirs() throws IOException {
        Files.createDirectories(getAppHome().resolve("data"));
        Files.createDirectories(getImagesDir());
    }
}
