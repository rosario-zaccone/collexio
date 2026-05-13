package org.collexio.presentation.view.itemcollection;

import org.collexio.presentation.view.MyForm;

import javax.swing.*;

public class UpdateItemCollectionForm extends MyForm {
    private final JTextField idField;
    private final JTextField nameField;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    public UpdateItemCollectionForm() {
        super("Update collection");

        idField = createTextField();
        idField.setEditable(false);
        idField.setEditable(false);
        nameField = createTextField();
        messageLabel = createMessageLabel();
        submitButton = createButton("Update");
        cancelButton = createSecondaryButton("Cancel");

        addField("ID:", idField);
        addField("Name:", nameField);
        addMessage(messageLabel);
        addButtonPanel(createButtonPanel(submitButton, cancelButton));
        finishForm();
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
