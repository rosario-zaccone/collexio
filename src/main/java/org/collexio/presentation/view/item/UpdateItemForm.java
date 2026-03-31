package org.collexio.presentation.view.item;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class UpdateItemForm extends JDialog {
    private final JComboBox<String> statusCombo;
    private final JTextField idField;
    private final JTextField collectionField;
    private final JTextField specField;
    private final JTextField photoIdField;
    private final JTextField photoPath;
    private final JTextField photoDate;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    public UpdateItemForm() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Id
        contentPanel.add(new JLabel("Item id:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(20);
        contentPanel.add(idField, gbc);

        // Collection
        gbc.gridx = 0;
        gbc.gridy++;
        contentPanel.add(new JLabel("Collection Id (Leave empty if the item isn't part of a collection:"), gbc);
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

        // Hidden photo id field
        photoIdField = new JTextField();
        photoIdField.setEditable(false);
        photoIdField.setVisible(false);
        contentPanel.add(photoIdField);

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
        submitButton = new JButton("Update");
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

    public String getId() {
        return idField.getText().trim();
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

    public void setId(String id) {
        idField.setText(id);
    }

    public void setCollection(String id) {
        collectionField.setText(id);
    }

    public void setSpec(String spec) {
        specField.setText(spec);
    }

    public void setStatus(int status) {
        statusCombo.setSelectedIndex(status);
    }

    public void setPhotoPath(String path) {
        photoPath.setText(path);
    }

    public void setPhotoDate(String date) {
        photoDate.setText(date);
    }

    public void setPhotoId(String id) {
        photoIdField.setText(id);
    }

    public String getPhotoId() {
        return photoIdField.getText().trim();
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
