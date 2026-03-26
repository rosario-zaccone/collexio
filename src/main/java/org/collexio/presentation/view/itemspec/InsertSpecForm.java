package org.collexio.presentation.view.itemspec;

import javax.swing.*;
import java.awt.*;


public class InsertSpecForm extends JDialog {
    private final JTextField typeField;
    private final JTextField nameField;
    private final JTextArea descriptionField;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    public InsertSpecForm() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Type
        contentPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        typeField = new JTextField(20);
        contentPanel.add(typeField, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy++;
        contentPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        contentPanel.add(nameField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(new JLabel("Description:"), gbc);

        gbc.gridy++;
        descriptionField = new JTextArea(5, 30);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionField);
        contentPanel.add(scrollPane, gbc);

        // Message label
        gbc.gridy++;
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
        nameField.setText("");
        messageLabel.setText(" ");
    }

    public String getName() {
        return nameField.getText().trim();
    }

    public String getItemType() {
        return typeField.getText().trim();
    }

    public String getDescription() {
        return descriptionField.getText().trim();
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