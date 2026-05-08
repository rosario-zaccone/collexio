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

    // ===================== API KEY =====================

    public String getApiKey() {
        return props.getProperty("api.key", "");
    }

    public void setApiKey(String key) {
        props.setProperty("api.key", key);
    }

    // ===================== CONTACT EMAIL =====================

    public String getContactEmail() {
        return props.getProperty("contact.email", "");
    }

    public void setContactEmail(String email) {
        props.setProperty("contact.email", email);
    }

    /**
     * Prompts the user for required configuration on first launch.
     * Throws IllegalStateException if user cancels.
     */
    public void ensureInitialConfig() throws IOException {

        if (getApiKey().isBlank() || getContactEmail().isBlank()) {

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JTextField apiKeyField = new JTextField();
            JTextField emailField = new JTextField();

            panel.add(new JLabel("Enter your Gemini API key:"));
            panel.add(apiKeyField);

            panel.add(Box.createVerticalStrut(10));

            panel.add(new JLabel("Enter contact email (for Wikipedia scraping):"));
            panel.add(emailField);

            int result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "Initial Setup",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) {
                throw new IllegalStateException(
                        "API key and contact email are required to run Collexio."
                );
            }

            String apiKey = apiKeyField.getText().trim();
            String email = emailField.getText().trim();

            if (apiKey.isBlank() || email.isBlank()) {
                throw new IllegalStateException("Both API key and email are required.");
            }

            setApiKey(apiKey);
            setContactEmail(email);
            save();
        }
    }
}