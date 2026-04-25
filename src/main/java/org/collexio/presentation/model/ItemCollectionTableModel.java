package org.collexio.presentation.model;

import org.collexio.business.domain.ItemCollection;
import org.collexio.business.service.ItemCollectionOrchestrator;
import org.collexio.business.service.ItemCollectionService;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ItemCollectionTableModel extends AbstractTableModel implements CrudTableModel<ItemCollection> {
    private final static int COLUMNS = 6;
    private List<ItemCollection> data;
    private final ItemCollectionService service;
    private final ItemCollectionOrchestrator orchestrator;

    public ItemCollectionTableModel(ItemCollectionService service, ItemCollectionOrchestrator orchestrator) throws SQLException {
        this.service = service;
        data = service.getAll();
        this.orchestrator = orchestrator;
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
        ItemCollection coll = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> coll.getId();
            case 1 -> coll.getName();
            case 2-> coll.length();
            case 3 -> "Items";
            case 4 -> "Update";
            case 5 -> "Delete";
            default -> throw new IllegalStateException("Unexpected value");
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Id";
            case 1 -> "Name";
            case 2 -> "Total";
            case 3-> "Items";
            case 4 -> "Update";
            case 5 -> "Delete";
            default -> "";
        };
    }

    public void removeRow(int row) throws SQLException {
        service.delete(data.get(row).getId());
        refresh();
        fireTableDataChanged();
    }

    @Override
    public void addRow(ItemCollection elem, Long associatedId) throws SQLException, IOException {
        service.add(elem);
        refresh();
        fireTableDataChanged();
    }

    @Override
    public void updateRow(ItemCollection elem, Long associatedId) throws SQLException, IOException {
        service.update(elem);
        refresh();
        fireTableDataChanged();
    }

    @Override
    public ItemCollection getRow(int row) {
        return data.get(row);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3 || columnIndex == 4 || columnIndex == 5;
    }

    public void refresh() throws SQLException {
        data = orchestrator.getAllFullCollections();
    }

    public List<Long> getCollectionIds() throws SQLException {
        return service.getAll().stream().map(ItemCollection::getId).toList();
    }

}
