package org.collexio.presentation.view;

import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.model.CrudTableModel;

import javax.swing.*;
import java.awt.*;

public class ItemSpecPanel extends JPanel{
    private final CrudTableModel model;
    private final JTable table;

    private final ButtonColumn deleteButtonColumn;
    private final JButton addButton;
    private final InsertSpecForm insertForm;

    public ItemSpecPanel(ItemSpecTableModel model) {
        this.model = model;
        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        deleteButtonColumn = new ButtonColumn(table, null, 4);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(600, 200));

        addButton = new JButton("Add");
        addButton.setAlignmentX(LEFT_ALIGNMENT);
        addButton.setMaximumSize(new Dimension(100, 30));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(scrollPane);
        add(Box.createVerticalStrut(10));
        add(addButton);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.insertForm = new InsertSpecForm();
    }

    public CrudTableModel getModel() {
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

    public ButtonColumn getDeleteButtonColumn() {
        return deleteButtonColumn;
    }

}
