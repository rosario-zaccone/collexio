package org.collexio.presentation.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;

public class ButtonColumn extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener {
    private final JTable table;
    private Action action;
    private int mnemonic;
    private Border focusBorder;
    private final JPanel renderPanel;
    private final JButton renderButton;
    private final JButton editButton;
    private Object editorValue;
    private boolean isButtonColumnEditor;

    public ButtonColumn(JTable table, Action action, int column) {
        this.table = table;
        this.action = action;

        renderPanel = new JPanel(new GridBagLayout());
        renderPanel.setOpaque(false);
        renderButton = createButton("");
        editButton = createButton("");
        editButton.addActionListener(this);
        setFocusBorder(MyPanel.createGlassBorder(18));

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
        columnModel.getColumn(column).setPreferredWidth(98);
        columnModel.getColumn(column).setMinWidth(78);
        table.addMouseListener(this);
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Border getFocusBorder() {
        return focusBorder;
    }

    public void setFocusBorder(Border focusBorder) {
        this.focusBorder = focusBorder;
        editButton.setBorder(focusBorder);
    }

    public int getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(int mnemonic) {
        this.mnemonic = mnemonic;
        renderButton.setMnemonic(mnemonic);
        editButton.setMnemonic(mnemonic);
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {
        applyValue(editButton, value);
        this.editorValue = value;
        return editButton;
    }

    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        renderPanel.removeAll();
        renderPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        applyValue(renderButton, value);
        renderButton.setBorder(hasFocus ? focusBorder : BorderFactory.createEmptyBorder());
        renderPanel.add(renderButton);
        return renderPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int row = table.convertRowIndexToModel(table.getEditingRow());
        fireEditingStopped();

        if (action != null) {
            ActionEvent event = new ActionEvent(table, ActionEvent.ACTION_PERFORMED, "" + row);
            action.actionPerformed(event);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (table.isEditing() && table.getCellEditor() == this) {
            isButtonColumnEditor = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isButtonColumnEditor && table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        isButtonColumnEditor = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    private JButton createButton(String text) {
        JButton button = MyPanel.createAeroButton(text, Color.WHITE, new Color(0xaedfff));
        button.setFont(new Font("Segoe UI", Font.BOLD, 10));
        button.setPreferredSize(new Dimension(76, 28));
        button.setMargin(new Insets(0, 7, 1, 7));
        return button;
    }

    private void applyValue(JButton button, Object value) {
        String text = value == null ? "" : value.toString();
        button.setText(text);
        button.setIcon(null);

        if (text.toLowerCase().contains("delete")) {
            button.putClientProperty("aero.bottom", new Color(0xffb1a8));
        } else {
            button.putClientProperty("aero.bottom", new Color(0xaedfff));
        }
    }
}
