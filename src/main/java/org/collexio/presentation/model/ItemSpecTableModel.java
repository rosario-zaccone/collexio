package org.collexio.presentation.model;

import org.collexio.business.domain.ItemSpec;
import org.collexio.business.service.ItemSpecService;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ItemSpecTableModel extends AbstractTableModel implements CrudTableModel<ItemSpec> {
    private final static int COLUMNS = 7;
    private List<ItemSpec> data;
    private final ItemSpecService service;

    public ItemSpecTableModel(ItemSpecService service) throws SQLException {
        this.service = service;
        data = service.getAll();
    }


    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ItemSpec spec = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> spec.getId();
            case 1 -> spec.getType();
            case 2 -> spec.getName();
            case 3 -> spec.getDescription();
            case 4 -> "Scrape price";
            case 5 -> "Update";
            case 6 -> "Delete";
            default -> throw new IllegalStateException("Unexpected value");
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Id";
            case 1 -> "Type";
            case 2 -> "Name";
            case 3 -> "Description";
            case 4 -> "Scrape price";
            case 5 -> "Update";
            case 6 -> "Delete";
            default -> "";
        };
    }

    @Override
    public void removeRow(int row) throws SQLException {
        service.delete(data.get(row).getId());
        refresh();
        fireTableDataChanged();
    }

    @Override
    public void addRow(ItemSpec elem, Long associatedId) throws SQLException {
        service.add(elem);
        refresh();
        fireTableDataChanged();
    }

    @Override
    public void updateRow(ItemSpec elem) throws SQLException {
        service.update(elem);
        refresh();
        fireTableDataChanged();
    }

    @Override
    public ItemSpec getRow(int row) {
        return data.get(row);
    }

    public double price(int row) {
        return service.price(data.get(row));
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 4 || columnIndex == 5 || columnIndex == 6;
    }

    public void refresh() throws SQLException {
        data = service.getAll();
    }

    public String generateDescription(ItemSpec spec) throws IOException, InterruptedException {
        return service.generateDescriptionByAI(spec);
    }

}
