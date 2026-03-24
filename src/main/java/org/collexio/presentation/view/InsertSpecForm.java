package org.collexio.presentation.view;

import javax.swing.*;
import java.awt.*;


public class InsertSpecForm extends JDialog {
    private final JTextField typeField;
    private final JTextField nameField;
    private final JTextField descriptionField;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    public InsertSpecForm() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(new JLabel("Type:"));
        typeField = new JTextField(20);
        typePanel.add(typeField);
        contentPanel.add(typePanel);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        namePanel.add(nameField);
        contentPanel.add(namePanel);

        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField(20);
        descriptionPanel.add(descriptionField);
        contentPanel.add(descriptionPanel);

        messageLabel = new JLabel(" ");
        contentPanel.add(messageLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitButton = new JButton("Insert");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel);

        this.getContentPane().add(contentPanel);

        this.pack();
        this.setLocationRelativeTo(null);
    }

    private void clearForm() {
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