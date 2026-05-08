package org.collexio.presentation.view.itemspec;

import org.collexio.presentation.view.MyForm;

import javax.swing.*;

public class InsertSpecForm extends MyForm {
    private final JComboBox<String> typeCombo;
    private final JTextField nameField;
    private final JTextArea descriptionField;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    public InsertSpecForm() {
        super("Insert item specification");

        typeCombo = createComboBox(new String[]{"Plant", "Tech Item", "Book"});
        nameField = createTextField();
        descriptionField = createTextArea(5, 30);
        messageLabel = createMessageLabel();
        submitButton = createButton("Insert");
        cancelButton = createSecondaryButton("Cancel");

        addField("Type:", typeCombo);
        addField("Name:", nameField);
        addWideField("Description:", createTextAreaScrollPane(descriptionField));
        addMessage(messageLabel);
        addButtonPanel(createButtonPanel(submitButton, cancelButton));
        finishForm();
    }

    public void clearForm() {
        nameField.setText("");
        typeCombo.setSelectedIndex(0);
        descriptionField.setText("");
        messageLabel.setText(" ");
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
