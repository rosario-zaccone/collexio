package org.collexio.presentation.view;

import javax.swing.*;
import java.awt.*;

public abstract class MyForm extends JDialog {

    protected static final Color DANGER = new Color(0xc94b3d);

    private final JPanel formPanel;
    private final GridBagConstraints gbc;
    private int row;

    protected MyForm(String title) {
        super((Frame) null, "", false);
        setContentPane(new ModernPanel());
        getContentPane().setLayout(new BorderLayout());
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel card = MyPanel.createGlassPanel(8);
        card.setLayout(new BorderLayout(0, 12));

        formPanel = MyPanel.createGlassPanel(8);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 14, 16));
        card.add(formPanel, BorderLayout.CENTER);

        getContentPane().add(card, BorderLayout.CENTER);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    protected JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setFont(MyPanel.FONT);
        field.setForeground(MyPanel.TEXT);
        field.setBackground(MyPanel.FIELD);
        field.setBorder(BorderFactory.createCompoundBorder(
                MyPanel.createGlassBorder(MyPanel.isFlatTheme() ? 12 : 12),
                BorderFactory.createEmptyBorder(MyPanel.isFlatTheme() ? 6 : 4, 9, MyPanel.isFlatTheme() ? 6 : 4, 9)
        ));
        field.setPreferredSize(new Dimension(280, MyPanel.isFlatTheme() ? 38 : 30));
        return field;
    }

    protected JComboBox<String> createComboBox(String[] values) {
        String[] translatedValues = java.util.Arrays.stream(values)
                .map(PresentationText::text)
                .toArray(String[]::new);
        JComboBox<String> combo = new JComboBox<>(translatedValues);
        combo.putClientProperty("values.key", values.clone());
        combo.setFont(MyPanel.FONT);
        combo.setForeground(MyPanel.TEXT);
        combo.setBackground(MyPanel.FIELD);
        combo.setBorder(MyPanel.createGlassBorder(MyPanel.isFlatTheme() ? 12 : 12));
        combo.setPreferredSize(new Dimension(280, MyPanel.isFlatTheme() ? 38 : 30));
        return combo;
    }

    protected JTextArea createTextArea(int rows, int columns) {
        JTextArea area = new JTextArea(rows, columns);
        area.setFont(MyPanel.FONT);
        area.setForeground(MyPanel.TEXT);
        area.setBackground(MyPanel.FIELD);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(9, 11, 9, 11));
        return area;
    }

    protected JScrollPane createTextAreaScrollPane(JTextArea area) {
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(MyPanel.isFlatTheme()
                ? BorderFactory.createEmptyBorder()
                : MyPanel.createGlassBorder(14));
        scrollPane.setPreferredSize(new Dimension(380, 142));
        return scrollPane;
    }

    protected JButton createButton(String text) {
        JButton button = MyPanel.createAeroButton(
                PresentationText.text(text),
                Color.WHITE,
                MyPanel.actionColorFor(text)
        );
        button.putClientProperty("text.key", text);
        button.setPreferredSize(new Dimension(132, 40));
        return button;
    }

    protected JButton createSecondaryButton(String text) {
        JButton button = MyPanel.createAeroButton(
                PresentationText.text(text),
                Color.WHITE,
                MyPanel.actionColorFor(text)
        );
        button.putClientProperty("text.key", text);
        button.setPreferredSize(new Dimension(132, 40));
        return button;
    }

    protected JButton createBrowseButton() {
        JButton button = createSecondaryButton("Browse...");
        button.setPreferredSize(new Dimension(124, 38));
        return button;
    }

    protected JLabel createMessageLabel() {
        return new FeedbackLabel();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            refreshLanguage(getContentPane());
            refreshTheme(getContentPane());
            pack();
        }
        super.setVisible(visible);
    }

    protected void addField(String labelText, JComponent field) {
        JLabel label = createLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(field, gbc);
        row++;
    }

    protected void addField(String labelText, JComponent field, JComponent trailing) {
        JLabel label = createLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(field, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        formPanel.add(trailing, gbc);
        row++;
    }

    protected void addWideField(String labelText, JComponent field) {
        JLabel label = createLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(label, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(field, gbc);
        row++;
    }

    protected void addMessage(JLabel label) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(label, gbc);
        row++;
    }

    protected JPanel createButtonPanel(JButton submitButton, JButton cancelButton) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    protected JPanel createButtonPanel(JButton button) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(button);
        return buttonPanel;
    }

    protected void addButtonPanel(JPanel buttonPanel) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);
        row++;
    }

    protected void finishForm() {
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(PresentationText.text(text));
        label.putClientProperty("text.key", text);
        label.setFont(MyPanel.FONT_BOLD);
        label.setForeground(MyPanel.TEXT_MUTED);
        return label;
    }

    private void refreshTheme(Component component) {
        component.setFont(MyPanel.FONT);
        if (component instanceof FeedbackLabel label) {
            label.setFont(MyPanel.FONT_BOLD);
        } else if (component instanceof JTextField
                || component instanceof JTextArea
                || component instanceof JComboBox<?>) {
            component.setForeground(MyPanel.TEXT);
            component.setBackground(MyPanel.FIELD);
        } else if (component instanceof JLabel label && !(label instanceof FeedbackLabel)) {
            label.setForeground(MyPanel.TEXT_MUTED);
            label.setFont(MyPanel.FONT_BOLD);
        } else if (component instanceof AbstractButton button) {
            button.setFont(MyPanel.FONT_BOLD);
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                refreshTheme(child);
            }
        }
    }

    private void refreshLanguage(Component component) {
        if (component instanceof AbstractButton button) {
            Object key = button.getClientProperty("text.key");
            if (key instanceof String textKey) {
                button.setText(PresentationText.text(textKey));
                button.putClientProperty("button.color", MyPanel.actionColorFor(textKey));
            }
        } else if (component instanceof JLabel label) {
            Object key = label.getClientProperty("text.key");
            if (key instanceof String textKey) {
                label.setText(PresentationText.text(textKey));
            }
        } else if (component instanceof JComboBox<?> combo) {
            Object key = combo.getClientProperty("values.key");
            if (key instanceof String[] values) {
                int selectedIndex = combo.getSelectedIndex();
                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(
                        java.util.Arrays.stream(values)
                                .map(PresentationText::text)
                                .toArray(String[]::new)
                );
                @SuppressWarnings("unchecked")
                JComboBox<String> typedCombo = (JComboBox<String>) combo;
                typedCombo.setModel(model);
                if (selectedIndex >= 0 && selectedIndex < typedCombo.getItemCount()) {
                    typedCombo.setSelectedIndex(selectedIndex);
                }
            }
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                refreshLanguage(child);
            }
        }
    }

    private static class ModernPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            MyPanel.paintAeroBackground((Graphics2D) g, getWidth(), getHeight());
        }
    }

    private static class FeedbackLabel extends JLabel {
        FeedbackLabel() {
            super(" ");
            setFont(MyPanel.FONT_BOLD);
            setForeground(MyPanel.TEXT_MUTED);
            setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
        }

        @Override
        public void setText(String text) {
            super.setText(text);
            String normalized = text == null ? "" : text.trim().toLowerCase();
            if (normalized.isEmpty()) {
                setForeground(MyPanel.TEXT_MUTED);
            } else if (normalized.contains("error") || normalized.contains("errore") || normalized.contains("input")) {
                setForeground(DANGER);
            } else {
                setForeground(MyPanel.isFlatTheme() ? MyPanel.LINK_BLUE : MyPanel.OTHER_AZURE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            String text = getText() == null ? "" : getText().trim();
            if (!text.isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                if (MyPanel.isFlatTheme()) {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color fill = getForeground().equals(DANGER)
                            ? new Color(0xffe2df)
                            : new Color(0xf0ebff);
                    g2.setColor(fill);
                    g2.fillRoundRect(0, 2, getWidth() - 1, getHeight() - 5, 12, 12);
                } else {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color fill = getForeground().equals(DANGER)
                            ? new Color(0xffd6d1)
                            : new Color(0xd9f6e4);
                    g2.setColor(fill);
                    g2.fillRoundRect(0, 3, getWidth() - 1, getHeight() - 7, 12, 12);
                    g2.setPaint(new GradientPaint(
                            0,
                            3,
                            new Color(0xff, 0xff, 0xff, 155),
                            0,
                            Math.max(4, getHeight() / 2),
                            new Color(0xff, 0xff, 0xff, 25)
                    ));
                    g2.fillRoundRect(2, 5, getWidth() - 5, Math.max(7, getHeight() / 2), 10, 10);
                }
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }
}
