package org.collexio.presentation.view.item;



import org.collexio.presentation.model.ItemCollectionTableModel;
import org.collexio.presentation.model.ItemTableModel;
import org.collexio.presentation.view.ButtonColumn;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.print.PrinterException;
import java.sql.SQLException;

public class ItemPanel extends JPanel{
    private final ItemTableModel model;
    private final JTable table;
    private final ItemCollectionTableModel collectionModel;
    private final ButtonColumn deleteButton;
    private final ButtonColumn updateButton;
    private final ButtonColumn priceButton;
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
        filter = new TableRowSorter<>(this.model);
        table.setRowSorter(filter);
        table.setRowHeight(60);
        table.getTableHeader().setReorderingAllowed(false);
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
        refreshFilter();
        this.insertForm = new InsertItemForm();
        this.updateForm = new UpdateItemForm();

        priceButton = new ButtonColumn(table, null, 6);
        updateButton = new ButtonColumn(table, null, 7);
        deleteButton = new ButtonColumn(table, null, 8);


        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel filterLabel = new JLabel("Filter by Collection ID: ");
        filterLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        filterPanel.add(filterLabel);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(filterField);


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        addButton = new JButton("Add");
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.setFont(addButton.getFont().deriveFont(Font.BOLD, 14f));

        printButton = new JButton("Print");
        printButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        printButton.setFont(printButton.getFont().deriveFont(Font.BOLD, 14f));

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomPanel.add(addButton);
        bottomPanel.add(printButton);
        add(bottomPanel, BorderLayout.SOUTH);
        filterPanel.setPreferredSize(new Dimension(0, 30));
        addButton.setPreferredSize(new Dimension(0, 50));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
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

