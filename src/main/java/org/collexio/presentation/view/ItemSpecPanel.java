package org.collexio.presentation.view;

import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.model.CrudTableModel;

import javax.swing.*;
import java.awt.*;

public class ItemSpecPanel extends JPanel{
    private final CrudTableModel model;
    private final JTable table;

    private final ButtonColumn deleteButton;
    private final ButtonColumn updateButton;
    private final ButtonColumn priceButton;
    private final JButton addButton;
    private final InsertSpecForm insertForm;
    private final UpdateSpecForm updateSpecForm;

    public ItemSpecPanel(ItemSpecTableModel model) {
        this.model = model;
        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        priceButton = new ButtonColumn(table, null, 4);
        updateButton = new ButtonColumn(table, null, 5);
        deleteButton = new ButtonColumn(table, null, 6);

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
        this.updateSpecForm = new UpdateSpecForm();
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
