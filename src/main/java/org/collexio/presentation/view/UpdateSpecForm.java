package org.collexio.presentation.view;

import javax.swing.*;
import java.awt.*;

public class UpdateSpecForm extends JDialog {
    private final JTextField idField;
    private final JTextField typeField;
    private final JTextField nameField;
    private final JTextArea descriptionField;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    private final JButton generateDescButton;

    public UpdateSpecForm() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idPanel.add(new JLabel("ID:"));
        idField = new JTextField(20);
        idField.setEditable(false);
        idPanel.add(idField);
        contentPanel.add(idPanel);

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

        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        descriptionPanel.add(new JLabel("Description:"));
        descriptionField = new JTextArea(5, 30);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionField);
        descriptionPanel.add(scrollPane);
        generateDescButton = new JButton("Generate description with AI");
        generateDescButton.setAlignmentX(Component.CENTER_ALIGNMENT); // centrato
        descriptionPanel.add(generateDescButton);
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

    public void clearForm() {
        idField.setText("");
        nameField.setText("");
        typeField.setText("");
        descriptionField.setText("");
        messageLabel.setText(" ");
    }

    public String getId() {
        return idField.getText().trim();
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

    public void setId(String id) {
        idField.setText(id);
    }

    public void setName(String name) {
        nameField.setText(name);
    }

    public void setItemType(String type) {
        typeField.setText(type);
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