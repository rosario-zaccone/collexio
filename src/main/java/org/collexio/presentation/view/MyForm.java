package org.collexio.presentation.view;

import javax.swing.*;
import java.awt.*;

public abstract class MyForm extends JDialog {

    protected static final Color TEXT = new Color(0x0a3a5a);
    protected static final Color TEXT_MUTED = new Color(0x4a7a8a);
    protected static final Color DANGER = new Color(0xff4433);
    protected static final Font FONT = new Font("Segoe UI", Font.PLAIN, 12);
    protected static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    protected static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 13);

    private final JPanel formPanel;
    private final GridBagConstraints gbc;
    private int row;

    protected MyForm(String title) {
        super((Frame) null, title, false);
        setContentPane(new AeroPanel());
        getContentPane().setLayout(new BorderLayout());
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel card = MyPanel.createGlassPanel(12);
        card.setLayout(new BorderLayout(0, 12));
        card.add(createTitleBar(title), BorderLayout.NORTH);

        formPanel = MyPanel.createGlassPanel(10);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 14, 16));
        card.add(formPanel, BorderLayout.CENTER);

        getContentPane().add(card, BorderLayout.CENTER);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    protected JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setFont(FONT);
        field.setForeground(TEXT);
        field.setBackground(new Color(255, 255, 255, 160));
        field.setBorder(BorderFactory.createCompoundBorder(
                MyPanel.createGlassBorder(20),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setPreferredSize(new Dimension(260, 34));
        return field;
    }

    protected JComboBox<String> createComboBox(String[] values) {
        JComboBox<String> combo = new JComboBox<>(values);
        combo.setFont(FONT);
        combo.setForeground(TEXT);
        combo.setBackground(new Color(255, 255, 255, 160));
        combo.setBorder(MyPanel.createGlassBorder(20));
        combo.setPreferredSize(new Dimension(260, 34));
        return combo;
    }

    protected JTextArea createTextArea(int rows, int columns) {
        JTextArea area = new JTextArea(rows, columns);
        area.setFont(FONT);
        area.setForeground(TEXT);
        area.setBackground(new Color(255, 255, 255, 165));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return area;
    }

    protected JScrollPane createTextAreaScrollPane(JTextArea area) {
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(MyPanel.createGlassBorder(10));
        scrollPane.setPreferredSize(new Dimension(360, 130));
        return scrollPane;
    }

    protected JButton createButton(String text) {
        JButton button = MyPanel.createAeroButton(text, Color.WHITE, new Color(0x9fe6c8));
        button.setPreferredSize(new Dimension(118, 36));
        return button;
    }

    protected JButton createSecondaryButton(String text) {
        JButton button = MyPanel.createAeroButton(text, Color.WHITE, new Color(0xaedfff));
        button.setPreferredSize(new Dimension(118, 36));
        return button;
    }

    protected JButton createBrowseButton() {
        JButton button = createSecondaryButton("Browse...");
        button.setPreferredSize(new Dimension(112, 34));
        return button;
    }

    protected JLabel createMessageLabel() {
        JLabel label = new JLabel(" ");
        label.setFont(FONT_BOLD);
        label.setForeground(DANGER);
        return label;
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
        JLabel label = new JLabel(text);
        label.setFont(FONT_BOLD);
        label.setForeground(TEXT_MUTED);
        return label;
    }

    private JPanel createTitleBar(String title) {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(0x64bedd), getWidth(), 0, new Color(0x3ca0d2)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 34));

        JLabel label = new JLabel(title);
        label.setFont(FONT_TITLE);
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

        bar.add(label, BorderLayout.CENTER);
        return bar;
    }

    private static class AeroPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            MyPanel.paintAeroBackground((Graphics2D) g, getWidth(), getHeight());
        }
    }
}
