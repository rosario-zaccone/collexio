package org.collexio.presentation.view.transaction;

import javax.swing.*;
import java.awt.*;

public class InsertTransactionForm extends JDialog {
    private final JComboBox<String> typeCombo;
    private final JTextField itemIdField;
    private final JTextField amountField;
    private final JTextField dateField;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    public InsertTransactionForm() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Item id
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
        itemIdField.setText("");
        amountField.setText("");
        typeCombo.setSelectedIndex(0);
        dateField.setText("");
        messageLabel.setText(" ");
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
