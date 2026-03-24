package org.collexio.presentation.model;

import org.collexio.business.domain.ItemSpec;
import org.collexio.business.service.ItemSpecService;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemSpecTableModel extends AbstractTableModel implements CrudTableModel {
    private final static int COLUMNS = 5;
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
            case 4 -> "Delete";
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
            case 4 -> "Delete";
            default -> "";
        };
    }

    public void removeRow(int row) throws SQLException {
        service.delete(data.get(row).getId());
        refresh();
        fireTableRowsDeleted(row, row);
    }

    @Override
    public void addRow(ItemSpec spec) throws SQLException {
        service.add(spec);
        refresh();
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 4;
    }

    public void refresh() throws SQLException {
        data = service.getAll();
    }
}
