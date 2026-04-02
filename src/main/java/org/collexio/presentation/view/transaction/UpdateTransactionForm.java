package org.collexio.presentation.view.transaction;

import javax.swing.*;
import java.awt.*;

public class UpdateTransactionForm extends JDialog {
    private final JComboBox<String> typeCombo;
    private final JTextField idField;
    private final JTextField itemIdField;
    private final JTextField amountField;
    private final JTextField dateField;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    public UpdateTransactionForm() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Transaction id
        contentPanel.add(new JLabel("Transaction id:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(20);
        contentPanel.add(idField, gbc);

        // Item id
        gbc.gridy++;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Item id:"), gbc);
        gbc.gridx = 1;
        itemIdField = new JTextField(20);
        contentPanel.add(itemIdField, gbc);

        // Amount
        gbc.gridy++;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        amountField = new JTextField(20);
        contentPanel.add(amountField, gbc);

        // Type
        gbc.gridy++;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        String[] statusOptions = {"Expense", "Income"};
        typeCombo = new JComboBox<>(statusOptions);
        contentPanel.add(typeCombo, gbc);

        // date with format hint
        gbc.gridx = 0;
        gbc.gridy++;
        contentPanel.add(new JLabel("Photo date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        dateField = new JTextField(20);
        contentPanel.add(dateField, gbc);

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
        itemIdField.setText("");
        amountField.setText("");
        typeCombo.setSelectedIndex(0);
        dateField.setText("");
        messageLabel.setText(" ");
    }

    public String getId() {
        return idField.getText().trim();
    }

    public String getItemId() {
        return itemIdField.getText().trim();
    }

    public String getAmount() {
        return amountField.getText().trim();
    }

    public int getTransactionType() {
        return typeCombo.getSelectedIndex();
    }

    public String getDate() {
        return dateField.getText().trim();
    }

    public void setId(String id) {
        idField.setText(id);
    }

    public void setAmount(String amount) {
        amountField.setText(amount);
    }

    public void setDate(String date) {
        dateField.setText(date);
    }

    public void setTransactionType(int type) {
        typeCombo.setSelectedIndex(type);
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
