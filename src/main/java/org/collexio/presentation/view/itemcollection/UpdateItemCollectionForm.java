package org.collexio.presentation.view.itemcollection;

import javax.swing.*;
import java.awt.*;

public class UpdateItemCollectionForm extends JDialog {
    private final JTextField idField;
    private final JTextField nameField;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    public UpdateItemCollectionForm() {
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
        contentPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        contentPanel.add(nameField, gbc);

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
        nameField.setText("");
        messageLabel.setText(" ");
    }

    public void setId(String id) {
        idField.setText(id);
    }

    public String getId() {
        return idField.getText();
    }


    public String getName() {
        return nameField.getText().trim();
    }

    public void setName(String name) {
        nameField.setText(name);
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
