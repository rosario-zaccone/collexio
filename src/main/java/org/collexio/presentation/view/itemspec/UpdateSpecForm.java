package org.collexio.presentation.view.itemspec;

import javax.swing.*;
import java.awt.*;

public class UpdateSpecForm extends JDialog {
    private final JTextField idField;
    private final JComboBox<String> typeCombo;
    private final JTextField nameField;
    private final JTextArea descriptionField;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    private final JButton generateDescButton;

    public UpdateSpecForm() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // ID
        contentPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(20);
        idField.setEditable(false);
        contentPanel.add(idField, gbc);

        // Type
        gbc.gridx = 0;
        gbc.gridy++;
        contentPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        String[] statusOptions = {"Book", "Tech Item", "Plant"};
        typeCombo = new JComboBox<>(statusOptions);
        contentPanel.add(typeCombo, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy++;
        contentPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        contentPanel.add(nameField, gbc);

        // Description Label
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(new JLabel("Description:"), gbc);

        // Description TextArea
        gbc.gridy++;
        descriptionField = new JTextArea(5, 30);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionField);
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(scrollPane, gbc);

        // Generate Description Button
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        generateDescButton = new JButton("Generate description with AI");
        JPanel generateButtonPanel = new JPanel();
        generateButtonPanel.add(generateDescButton);
        contentPanel.add(generateButtonPanel, gbc);

        // Message Label
        gbc.gridy++;
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        contentPanel.add(messageLabel, gbc);

        // Buttons
        gbc.gridy++;
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
        idField.setText("");
        nameField.setText("");
        typeCombo.setSelectedIndex(0);
        descriptionField.setText("");
        messageLabel.setText(" ");
    }

    public String getId() {
        return idField.getText().trim();
    }

    public String getName() {
        return nameField.getText().trim();
    }

    public int getItemType() {
        return typeCombo.getSelectedIndex();
    }

    public String getDescription() {
        return descriptionField.getText().trim();
    }

    public void setId(String id) {
        idField.setText(id);
    }

    public void setName(String name) {
        nameField.setText(name);
    }

    public void setItemType(int type) {
        typeCombo.setSelectedIndex(type);
    }

    public void setDescription(String description) {
        descriptionField.setText(description);
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

    public JButton getGenerateDescButton() {
        return generateDescButton;
    }
}