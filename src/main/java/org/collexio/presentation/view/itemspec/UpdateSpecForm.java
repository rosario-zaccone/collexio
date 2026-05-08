package org.collexio.presentation.view.itemspec;

import org.collexio.presentation.view.MyForm;

import javax.swing.*;

public class UpdateSpecForm extends MyForm {
    private final JTextField idField;
    private final JComboBox<String> typeCombo;
    private final JTextField nameField;
    private final JTextArea descriptionField;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    private final JButton generateDescButton;

    public UpdateSpecForm() {
        super("Update item specification");

        idField = createTextField();
        idField.setEditable(false);
        typeCombo = createComboBox(new String[]{"Plant", "Tech Item", "Book"});
        nameField = createTextField();
        descriptionField = createTextArea(5, 30);
        generateDescButton = createSecondaryButton("Generate description with AI");
        messageLabel = createMessageLabel();
        submitButton = createButton("Update");
        cancelButton = createSecondaryButton("Cancel");

        addField("ID:", idField);
        addField("Type:", typeCombo);
        addField("Name (latin name for plants):", nameField);
        addWideField("Description:", createTextAreaScrollPane(descriptionField));
        addButtonPanel(createButtonPanel(generateDescButton));
        addMessage(messageLabel);
        addButtonPanel(createButtonPanel(submitButton, cancelButton));
        finishForm();
    }

    public void clearForm() {
        idField.setText("");
        nameField.setText("");
        typeCombo.setSelectedIndex(0);
        descriptionField.setText("");
        messageLabel.setText(" ");
    }

    public String getId() {
        return idField.getText().trim();
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

    public void setId(String id) {
        idField.setText(id);
    }

    public void setName(String name) {
        nameField.setText(name);
    }

    public void setItemType(int type) {
        typeCombo.setSelectedIndex(type);
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
