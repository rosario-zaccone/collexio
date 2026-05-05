package org.collexio.presentation.view.item;



import org.collexio.presentation.model.ItemCollectionTableModel;
import org.collexio.presentation.model.ItemTableModel;
import org.collexio.presentation.view.MyPanel;
import org.collexio.presentation.view.ButtonColumn;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;

public class ItemPanel extends MyPanel {
    private final ItemTableModel model;
    private final JTable table;
    private final ItemCollectionTableModel collectionModel;
    private final ButtonColumn deleteButton;
    private final ButtonColumn updateButton;
    private final ButtonColumn priceButton;
    private final ButtonColumn transactionsButton;
    private final JButton printButton;
    private final JButton addButton;
    private final InsertItemForm insertForm;
    private final UpdateItemForm updateForm;
    private final JComboBox<String> filterField;

    TableRowSorter<ItemTableModel> filter;

    public ItemPanel(ItemTableModel model, ItemCollectionTableModel collectionModel) {
        this.collectionModel = collectionModel;
        filterField = new JComboBox<String>();
        this.model = model;

        table = createTable(model);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                int modelRow = table.convertRowIndexToModel(row);

                try {
                    var item = model.getRow(modelRow);

                    boolean available = model.isAvailable(item);

                    if (!isSelected) {
                        c.setBackground(available ? Color.WHITE : new Color(255, 200, 200));
                    } else {
                        c.setBackground(table.getSelectionBackground());
                    }

                } catch (Exception e) {
                    c.setBackground(Color.WHITE);
                }

                return c;
            }
        });

        filter = new TableRowSorter<>(this.model);
        table.setRowSorter(filter);
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

                int modelRow = table.convertRowIndexToModel(row);
                try {
                    var item = model.getRow(modelRow);
                    boolean available = model.isAvailable(item);
                    if (!isSelected) {
                        label.setBackground(available ? Color.WHITE : new Color(255, 200, 200));
                    } else {
                        label.setBackground(table.getSelectionBackground());
                    }
                } catch (Exception e) {
                    label.setBackground(Color.WHITE);
                }

                return label;
            }
        });
        refreshFilter();
        this.insertForm = new InsertItemForm();
        this.updateForm = new UpdateItemForm();

        transactionsButton = new ButtonColumn(table, null, 7);
        priceButton = new ButtonColumn(table, null, 8);
        updateButton = new ButtonColumn(table, null, 9);
        deleteButton = new ButtonColumn(table, null, 10);


        JPanel filterPanel = createFilterBar(filterField, "Filter by Collection ID:");


        JScrollPane scrollPane = createScrollPane(table, 700, 300);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        addButton = createButton("+ Add");
        printButton = createButton("Print");

        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.setFont(addButton.getFont().deriveFont(Font.BOLD, 14f));

        printButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        printButton.setFont(printButton.getFont().deriveFont(Font.BOLD, 14f));


        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomPanel.add(addButton);
        bottomPanel.add(printButton);
        add(bottomPanel, BorderLayout.SOUTH);
        addButton.setPreferredSize(new Dimension(0, 50));
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

    public UpdateItemForm getUpdateForm() {
         return updateForm;
     }

    public JButton getPrintButton() {
        return printButton;
    }

    public ButtonColumn getTransactionsButton() {
        return transactionsButton;
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

    public JComboBox<String> getFilterField() {
        return filterField;
    }

    public void setFilterField(RowFilter<ItemTableModel, Integer> rf) {
        filter.setRowFilter(rf);
    }

    public void setCollectionIdForFilter(String id) {
        filterField.setSelectedItem(id);
    }

    public void refreshFilter() {
        try {
            var ids = new java.util.ArrayList<>(
                    collectionModel.getCollectionIds().stream()
                            .map(Object::toString)
                            .toList()
            );
            ids.add(0, "");
            filterField.setModel(new DefaultComboBoxModel<>(ids.toArray(String[]::new)));
        } catch (SQLException e) {
            filterField.setModel(new DefaultComboBoxModel<>(new String[]{""}));
        }
    }
}

