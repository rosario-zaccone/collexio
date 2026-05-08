package org.collexio.presentation.view.item;

import org.collexio.presentation.view.MyForm;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class UpdateItemForm extends MyForm {
    private final JComboBox<String> statusCombo;
    private final JTextField idField;
    private final JTextField collectionField;
    private final JTextField specField;
    private final JTextField photoIdField;
    private final JTextField photoPath;
    private final JTextField photoDate;

    private final JButton submitButton;
    private final JButton cancelButton;
    private final JLabel messageLabel;

    public UpdateItemForm() {
        super("Update item");

        idField = createTextField();
        idField.setEditable(false);
        collectionField = createTextField();
        specField = createTextField();
        statusCombo = createComboBox(new String[]{"Bad", "Average", "Good"});
        photoPath = createTextField();
        JButton browseButton = createBrowseButton();
        photoDate = createTextField();
        photoIdField = createTextField();
        photoIdField.setEditable(false);
        photoIdField.setVisible(false);
        messageLabel = createMessageLabel();
        submitButton = createButton("Update");
        cancelButton = createSecondaryButton("Cancel");

        addField("Item id:", idField);
        addField("Collection Id (Leave empty if the item isn't part of a collection):", collectionField);
        addField("Item specification id:", specField);
        addField("Status:", statusCombo);
        addField("Photo:", photoPath, browseButton);
        addField("Photo date (YYYY-MM-DD):", photoDate);
        addMessage(messageLabel);
        addButtonPanel(createButtonPanel(submitButton, cancelButton));

        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Image Files", "jpg", "jpeg", "png", "gif", "bmp"
            );
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                photoPath.setText(selectedFile.getAbsolutePath());
            }
        });

        finishForm();
    }

    public void clearForm() {
        collectionField.setText("");
        specField.setText("");
        statusCombo.setSelectedIndex(0);
        photoPath.setText("");
        photoDate.setText("");
        messageLabel.setText(" ");
    }

    public String getId() {
        return idField.getText().trim();
    }

    public String getCollection() {
        return collectionField.getText().trim();
    }

    public String getSpec() {
        return specField.getText().trim();
    }

    public int getStatus() {
        return statusCombo.getSelectedIndex();
    }

    public String getPhotoPath() {
        return photoPath.getText().trim();
    }

    public String getPhotoDate() {
        return photoDate.getText().trim();
    }

    public void setId(String id) {
        idField.setText(id);
    }

    public void setCollection(String id) {
        collectionField.setText(id);
    }

    public void setSpec(String spec) {
        specField.setText(spec);
    }

    public void setStatus(int status) {
        statusCombo.setSelectedIndex(status);
    }

    public void setPhotoPath(String path) {
        photoPath.setText(path);
    }

    public void setPhotoDate(String date) {
        photoDate.setText(date);
    }

    public void setPhotoId(String id) {
        photoIdField.setText(id);
    }

    public String getPhotoId() {
        return photoIdField.getText().trim();
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
