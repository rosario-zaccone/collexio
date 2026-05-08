package org.collexio.presentation.view.transaction;

import org.collexio.presentation.view.MyForm;

import javax.swing.*;

public class UpdateTransactionForm extends MyForm {
    private final JComboBox<String> typeCombo;
    private final JTextField idField;
    private final JTextField itemIdField;
    private final JTextField amountField;
    private final JTextField dateField;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    public UpdateTransactionForm() {
        super("Update transaction");

        idField = createTextField();
        idField.setEditable(false);
        itemIdField = createTextField();
        amountField = createTextField();
        typeCombo = createComboBox(new String[]{"Expense", "Income"});
        dateField = createTextField();
        messageLabel = createMessageLabel();
        submitButton = createButton("Update");
        cancelButton = createSecondaryButton("Cancel");

        addField("Transaction id:", idField);
        addField("Item id:", itemIdField);
        addField("Amount:", amountField);
        addField("Type:", typeCombo);
        addField("Date (YYYY-MM-DD):", dateField);
        addMessage(messageLabel);
        addButtonPanel(createButtonPanel(submitButton, cancelButton));
        finishForm();
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

    public void setItemId(String itemId) {
        itemIdField.setText(itemId);
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
