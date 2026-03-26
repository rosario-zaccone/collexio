package org.collexio.presentation.view.itemcollection;

import javax.swing.*;
import java.awt.*;

public class InsertItemCollectionForm extends JDialog {
        private final JTextField nameField;
        private final JButton submitButton;
        private final JButton cancelButton;
        private final JLabel messageLabel;

        public InsertItemCollectionForm() {
            JPanel contentPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;

            // Type
            contentPanel.add(new JLabel("Name:"), gbc);
            gbc.gridx = 1;
            nameField = new JTextField(20);
            contentPanel.add(nameField, gbc);

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
