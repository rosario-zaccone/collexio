package org.collexio.presentation.model;

import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemSpec;
import org.collexio.business.service.ItemOrchestrator;
import org.collexio.business.service.ItemService;
import org.collexio.business.service.ItemSpecService;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ItemTableModel extends AbstractTableModel implements CrudTableModel<Item> {
    private final static int COLUMNS = 9;
    private List<Item> data;
    private final ItemService service;
    private final ItemOrchestrator orchestrator;
    private final ItemSpecService specService;

    public ItemTableModel(ItemService service, ItemOrchestrator orchestrator, ItemSpecService specService) throws SQLException {
        this.service = service;
        this.orchestrator = orchestrator;
        data = orchestrator.getAllFullItems();
        this.specService = specService;
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
        Item item = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> item.getId();
            case 1 -> {
                String path = item.getPhoto() != null ? String.valueOf(item.getPhoto().getPath()) : null;
                if (path != null) {
                    ImageIcon icon = new ImageIcon(path);
                    Image scaled = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    yield new ImageIcon(scaled);
                } else {
                    yield null;
                }
            }
            case 2 -> item.getSpec().getType();
            case 3 -> item.getSpec().getName();
            case 4 -> item.getSpec().getDescription();
            case 5 -> item.getStatus();
            case 6 -> "Scrape price";
            case 7 -> "Update";
            case 8 -> "Delete";
            default -> throw new IllegalStateException("Unexpected value");
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Id";
            case 1 -> "Photo";
            case 2 -> "Type";
            case 3 -> "Name";
            case 4 -> "Description";
            case 5 -> "Status";
            case 6 -> "Scrape price";
            case 7 -> "Update";
            case 8 -> "Delete";
            default -> "";
        };
    }

    @Override
    public void removeRow(int row) throws SQLException {
        service.delete(data.get(row).getId());
        refresh();
        fireTableRowsDeleted(row, row);
    }

    @Override
    public void addRow(Item elem) throws SQLException, IOException {
        orchestrator.addWithPhoto(elem, null, elem.getPhoto());
        refresh();
        fireTableDataChanged();
    }

    public void addRowWithCollection(Item elem, Long collectionId) throws SQLException, IOException {
        orchestrator.addWithPhoto(elem, collectionId, elem.getPhoto());
        refresh();
        fireTableDataChanged();
    }

    @Override
    public void updateRow(Item elem) throws SQLException, IOException { // free from collection
        orchestrator.updateWithPhoto(elem, null, elem.getPhoto());
        refresh();
        fireTableDataChanged();
    }


    public void updateRowWithCollection(Item elem, Long collectionId) throws SQLException, IOException {
        orchestrator.updateWithPhoto(elem, collectionId, elem.getPhoto());
        service.update(elem, collectionId);
        refresh();
        fireTableDataChanged();
    }

    @Override
    public Item getRow(int row) {
        return data.get(row);
    }

    public double price(int row) {
        return specService.price(data.get(row).getSpec());
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 8 || columnIndex == 7 || columnIndex == 6;
    }

    public void refresh() throws SQLException {
        data = orchestrator.getAllFullItems();
    }

    public ItemSpec getSpec(Long specId) throws SQLException {
        return specService.get(specId);
    }

    public Optional<Long> getCollectionId(Item elem) throws SQLException {
        return service.getCollectionId(elem);
    }
}
