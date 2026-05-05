package org.collexio;


import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Properties;

public class ConfigManager {

    private final Properties props = new Properties();

    public void load() throws IOException {
        if (Files.exists(AppConfig.getConfigFile())) {
            try (InputStream in = Files.newInputStream(AppConfig.getConfigFile())) {
                props.load(in);
            }
        }
    }

    public void save() throws IOException {
        try (OutputStream out = Files.newOutputStream(AppConfig.getConfigFile())) {
            props.store(out, "Collexio config");
        }
    }

    public String getApiKey() {
        return props.getProperty("api.key", "");
    }

    public void setApiKey(String key) {
        props.setProperty("api.key", key);
    }

    /**
     * Prompts the user for the Gemini API key on first launch.
     * Throws IllegalStateException if the user cancels — app cannot run without it.
     */
    public void ensureApiKey() throws IOException {
        if (!getApiKey().isBlank()) return;

        String key = JOptionPane.showInputDialog(
                null,
                "Enter your Gemini API key:",
                "Initial Setup",
                JOptionPane.PLAIN_MESSAGE
        );

        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                    "API key is required to run Collexio."
            );
        }

        setApiKey(key.trim());
        save();
    }
}
