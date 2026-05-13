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

        if (getContactEmail().isBlank()) {

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JTextField apiKeyField = new JTextField();
            JTextField emailField = new JTextField();
            JCheckBox skipApiKey = new JCheckBox("Continue without Gemini API key");
            skipApiKey.addActionListener(e -> apiKeyField.setEnabled(!skipApiKey.isSelected()));

            panel.add(new JLabel("Gemini API key (optional):"));
            panel.add(apiKeyField);
            panel.add(new JLabel("Used only to generate item descriptions with AI."));
            panel.add(skipApiKey);

            panel.add(Box.createVerticalStrut(10));

            panel.add(new JLabel("Contact email:"));
            panel.add(emailField);
            panel.add(new JLabel("Required for scraping requests."));

            int result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "Initial Setup",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) {
                throw new IllegalStateException(
                        "Contact email is required to run Collexio."
                );
            }

            String apiKey = apiKeyField.getText().trim();
            String email = emailField.getText().trim();

            if (email.isBlank()) {
                throw new IllegalStateException("Contact email is required.");
            }

            setApiKey(apiKey);
            setContactEmail(email);
            save();
        }
    }
}
