package org.collexio.presentation.view.item;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class InsertItemForm extends JDialog {
    private final JComboBox<String> statusCombo;
    private final JTextField collectionField;
    private final JTextField specField;
    private final JTextField photoPath;
    private final JTextField photoDate;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    public InsertItemForm() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Collection
        contentPanel.add(new JLabel("Collection Id(Leave empty if the item isn't part of a collection) :"), gbc);
        gbc.gridx = 1;
        collectionField = new JTextField(20);
        contentPanel.add(collectionField, gbc);

        // Spec
        gbc.gridx = 0;
        gbc.gridy++;
        contentPanel.add(new JLabel("Item specification id:"), gbc);
        gbc.gridx = 1;
        specField = new JTextField(20);
        contentPanel.add(specField, gbc);

        // Status as JComboBox
        gbc.gridx = 0;
        gbc.gridy++;
        contentPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        String[] statusOptions = {"Bad", "Average", "Good"};
        statusCombo = new JComboBox<>(statusOptions);
        contentPanel.add(statusCombo, gbc);

        // Photo path with file chooser
        gbc.gridx = 0;
        gbc.gridy++;
        contentPanel.add(new JLabel("Photo:"), gbc);
        gbc.gridx = 1;
        photoPath = new JTextField(20);
        contentPanel.add(photoPath, gbc);

        gbc.gridx = 2;
        JButton browseButton = new JButton("Browse...");
        contentPanel.add(browseButton, gbc);

        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Image Files", "jpg", "jpeg", "png", "gif", "bmp"
            );
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                photoPath.setText(selectedFile.getAbsolutePath());
            }
        });

        // Photo date with format hint
        gbc.gridx = 0;
        gbc.gridy++;
        contentPanel.add(new JLabel("Photo date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        photoDate = new JTextField(20);
        contentPanel.add(photoDate, gbc);

        // Message label
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        contentPanel.add(messageLabel, gbc);

        // Buttons
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        submitButton = new JButton("Insert");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel, gbc);

        this.getContentPane().add(contentPanel);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void clearForm() {
        collectionField.setText("");
        specField.setText("");
        statusCombo.setSelectedIndex(0);
        photoPath.setText("");
        photoDate.setText("");
        messageLabel.setText(" ");
    }

    public String getCollection() {
        return collectionField.getText().trim();
    }

    public String getSpec() {
        return specField.getText().trim();
    }

    public int getStatus() {
        return statusCombo.getSelectedIndex();
    }

    public String getPhotoPath() {
        return photoPath.getText().trim();
    }

    public String getPhotoDate() {
        return photoDate.getText().trim();
    }

    public void setMessageLabel(String text) {
        messageLabel.setText(text);
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
}