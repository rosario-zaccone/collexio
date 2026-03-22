package org.collexio.presentation.view;

import org.collexio.presentation.model.ItemSpecTableModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class ItemSpecPanel extends JPanel{
    private final AbstractTableModel model;
    private final JTable table;

    public ItemSpecPanel(ItemSpecTableModel model) {
        this.model = model;
        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }
}
