package org.collexio.presentation.model;

import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemSpec;
import org.collexio.business.service.ItemOrchestrator;
import org.collexio.business.service.ItemService;
import org.collexio.business.service.ItemSpecService;
import org.collexio.business.service.PriceService;
import org.collexio.utilities.factory.AbstractFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ItemTableModel extends AbstractTableModel implements CrudTableModel<Item> {
    private final static int COLUMNS = 11;
    private List<Item> data;
    private final ItemService service;
    private final ItemOrchestrator orchestrator;
    private final ItemSpecService specService;
    private final PriceService priceService;

    public ItemTableModel(ItemService service, ItemOrchestrator orchestrator, ItemSpecService specService, PriceService priceService) throws SQLException {
        this.service = service;
        this.orchestrator = orchestrator;
        data = orchestrator.getAllFullItems();
        this.specService = specService;
        this.priceService = priceService;

    }


    @Override
    public int getRowCount() {
        return data == null ? 0 : data.size();
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
            case 6 -> {
                try {
                    yield service.getCollectionId(item.getId()).isEmpty() ? "No collection" : service.getCollectionId(item.getId()).get() ;
                } catch (SQLException e) {
                    yield "Not available";
                }
            }
            case 7 -> "Transactions";
            case 8 -> "Scrape price";
            case 9 -> "Update";
            case 10 -> "Delete";
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
            case 6 -> "Collection id";
            case 7 -> "Transactions";
            case 8 -> "Scrape price";
            case 9 -> "Update";
            case 10 -> "Delete";
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
    public void addRow(Item elem, Long collectionId) throws SQLException, IOException {
        orchestrator.addWithPhoto(elem, collectionId, elem.getPhoto());
        refresh();
        fireTableDataChanged();
    }



    @Override
    public void updateRow(Item elem, Long collectionId) throws SQLException, IOException {
        orchestrator.updateWithPhoto(elem, collectionId, elem.getPhoto());
        service.update(elem, collectionId);
        refresh();
        fireTableDataChanged();
    }

    @Override
    public Item getRow(int row) {
        return data.get(row);
    }

    public double price(int row) throws SQLException, InterruptedException {
        ItemSpec spec = specService.getByItemId(data.get(row).getId());
        priceService.setScraper(spec.getType());
        return priceService.computePrice(spec.getName());
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex <= 10 && columnIndex >= 7;
    }

    public void refresh() throws SQLException {
        data = orchestrator.getAllFullItems();
    }

    public ItemSpec getSpec(Long specId) throws SQLException {
        return specService.get(specId);
    }

    public Optional<Long> getCollectionId(Long itemId) throws SQLException {
        return service.getCollectionId(itemId);
    }


    public List<Long> getItemIds() throws SQLException {
        return service.getAll().stream().map(Item::getId).toList();
    }

    public boolean isAvailable(Item item) {
        return service.isAvailable(item);
    }
}
