package org.collexio.presentation.view.item;



import org.collexio.presentation.model.ItemTableModel;
import org.collexio.presentation.view.ButtonColumn;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ItemPanel extends JPanel{
    private final ItemTableModel model;
    private final JTable table;

    private final ButtonColumn deleteButton;
    private final ButtonColumn updateButton;
    private final ButtonColumn priceButton;
    private final JButton addButton;
    private final InsertItemForm insertForm;
    // private final UpdateSpecForm updateSpecForm;

    public ItemPanel(ItemTableModel model) {
        this.model = model;

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                if (c instanceof JComponent) {
                    Object value = getValueAt(row, column);
                    if (value != null) {
                        String htmlText = "<html><body style='width: 300px;'>" + value + "</body></html>";
                        ((JComponent) c).setToolTipText(htmlText);
                    } else {
                        ((JComponent) c).setToolTipText(null);
                    }
                }

                return c;
            }
        };

        table.setRowHeight(60);
        table.getTableHeader().setReorderingAllowed(false);

        priceButton = new ButtonColumn(table, null, 6);
        updateButton = new ButtonColumn(table, null, 7);
        deleteButton = new ButtonColumn(table, null, 8);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        addButton = new JButton("Add");
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.setMaximumSize(new Dimension(120, 35));
        addButton.setFont(addButton.getFont().deriveFont(Font.BOLD, 14f));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(scrollPane);
        add(Box.createVerticalStrut(15));
        add(addButton);

        this.insertForm = new InsertItemForm();
        // this.updateSpecForm = new UpdateSpecForm();

        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (value instanceof ImageIcon icon) {
                    label.setIcon(icon);
                    label.setText(null);
                    label.setHorizontalAlignment(JLabel.CENTER);
                } else {
                    label.setIcon(null);
                    label.setText("No image");
                }

                return label;
            }
        });
    }

    public ItemTableModel getModel() {
        return model;
    }

    public JTable getTable() {
        return table;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public InsertItemForm getInsertForm() {
        return insertForm;
    }

    // public UpdateForm getUpdateSpecForm() {
    //     return updateSpecForm;
    // }

    public ButtonColumn getPriceButton() {
        return priceButton;
    }

    public ButtonColumn getDeleteButton() {
        return deleteButton;
    }

    public ButtonColumn getUpdateButton() {
        return updateButton;
    }
}

