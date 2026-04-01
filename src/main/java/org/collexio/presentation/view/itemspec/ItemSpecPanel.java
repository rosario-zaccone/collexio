package org.collexio.presentation.view.itemspec;

import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.model.CrudTableModel;
import org.collexio.presentation.view.ButtonColumn;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ItemSpecPanel extends JPanel{
    private final ItemSpecTableModel model;
    private final JTable table;

    private final ButtonColumn deleteButton;
    private final ButtonColumn updateButton;
    private final ButtonColumn priceButton;
    private final JButton addButton;
    private final InsertSpecForm insertForm;
    private final UpdateSpecForm updateSpecForm;

    public ItemSpecPanel(ItemSpecTableModel model) {
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

        priceButton = new ButtonColumn(table, null, 4);
        updateButton = new ButtonColumn(table, null, 5);
        deleteButton = new ButtonColumn(table, null, 6);
        this.insertForm = new InsertSpecForm();
        this.updateSpecForm = new UpdateSpecForm();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        addButton = new JButton("Add");
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.setMaximumSize(new Dimension(120, 35));
        addButton.setFont(addButton.getFont().deriveFont(Font.BOLD, 14f));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(scrollPane, BorderLayout.CENTER);
        add(addButton, BorderLayout.SOUTH);
        addButton.setPreferredSize(new Dimension(0, 50));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

    }

    public ItemSpecTableModel getModel() {
        return model;
    }

    public JTable getTable() {
        return table;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public InsertSpecForm getInsertForm() {
        return insertForm;
    }

    public UpdateSpecForm getUpdateSpecForm() {
        return updateSpecForm;
    }

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
