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

        filter = new TableRowSorter<>(this.model);
        table.setRowSorter(filter);
        table.setRowHeight(84);
        table.getColumnModel().getColumn(1).setPreferredWidth(108);
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Color background = isSelected ? table.getSelectionBackground() : getRowBackground(table, row);
                return new ImagePreview(value instanceof ImageIcon icon ? icon : null, background);
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
        addButton.setFont(addButton.getFont().deriveFont(Font.BOLD, 15f));

        printButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        printButton.setFont(printButton.getFont().deriveFont(Font.BOLD, 15f));


        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setOpaque(false);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.add(addButton);
        bottomPanel.add(printButton);
        add(bottomPanel, BorderLayout.SOUTH);
        addButton.setPreferredSize(new Dimension(0, 58));
        printButton.setPreferredSize(new Dimension(0, 58));
    }

    @Override
    protected Color getRowBackground(JTable table, int row) {
        int modelRow = table.convertRowIndexToModel(row);
        try {
            var item = model.getRow(modelRow);
            return model.isAvailable(item) ? super.getRowBackground(table, row) : new Color(0xf4b1ac);
        } catch (Exception e) {
            return super.getRowBackground(table, row);
        }
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

    private static class ImagePreview extends JPanel {
        private final ImageIcon icon;

        ImagePreview(ImageIcon icon, Color background) {
            this.icon = icon;
            setOpaque(true);
            setBackground(background);
            setPreferredSize(new Dimension(96, 70));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int boxWidth = 66;
            int boxHeight = 54;
            int x = (getWidth() - boxWidth) / 2;
            int y = (getHeight() - boxHeight) / 2;

            g2.setColor(Color.WHITE);
            g2.fillRect(x, y, boxWidth, boxHeight);
            g2.setColor(new Color(0xd0d0d0));
            g2.drawRect(x, y, boxWidth, boxHeight);

            if (icon != null) {
                Image image = icon.getImage();
                int imageWidth = icon.getIconWidth();
                int imageHeight = icon.getIconHeight();
                double scale = Math.min((double) (boxWidth - 10) / imageWidth, (double) (boxHeight - 10) / imageHeight);
                int drawWidth = Math.max(1, (int) Math.round(imageWidth * scale));
                int drawHeight = Math.max(1, (int) Math.round(imageHeight * scale));
                int drawX = x + (boxWidth - drawWidth) / 2;
                int drawY = y + (boxHeight - drawHeight) / 2;
                g2.drawImage(image, drawX, drawY, drawWidth, drawHeight, this);
            } else {
                g2.setColor(MyPanel.LINK_BLUE);
                g2.drawRect(x + 17, y + 14, 22, 16);
                g2.fillOval(x + 24, y + 18, 5, 5);
                g2.drawLine(x + 20, y + 29, x + 27, y + 24);
                g2.drawLine(x + 27, y + 24, x + 36, y + 31);
            }
            g2.dispose();
        }
    }
}
