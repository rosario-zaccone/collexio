package org.collexio.presentation.view.itemcollection;

import org.collexio.presentation.view.MyForm;

import javax.swing.*;

public class InsertItemCollectionForm extends MyForm {
    private final JTextField nameField;
    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    public InsertItemCollectionForm() {
        super("Insert collection");

        nameField = createTextField();
        messageLabel = createMessageLabel();
        submitButton = createButton("Insert");
        cancelButton = createSecondaryButton("Cancel");

        addField("Name:", nameField);
        addMessage(messageLabel);
        addButtonPanel(createButtonPanel(submitButton, cancelButton));
        finishForm();
    }

    public void clearForm() {
        nameField.setText("");
        messageLabel.setText(" ");
    }

    public String getName() {
        return nameField.getText().trim();
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
