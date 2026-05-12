package org.collexio.presentation.model;

import org.collexio.business.domain.ItemSpec;
import org.collexio.business.service.InfoGenerationService;
import org.collexio.business.service.ItemSpecService;
import org.collexio.business.service.PriceService;
import org.collexio.presentation.view.PresentationText;
import org.collexio.utilities.factory.AbstractFactory;
import org.collexio.utilities.infogenerator.InfoGenerator;
import org.collexio.utilities.pricecraper.PriceScraper;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ItemSpecTableModel extends AbstractTableModel implements CrudTableModel<ItemSpec> {
    private final static int COLUMNS = 7;
    private List<ItemSpec> data;
    private final ItemSpecService service;
    private final PriceService priceService;
    private final InfoGenerationService infoService;

    public ItemSpecTableModel(ItemSpecService service, PriceService priceService, InfoGenerationService infoService) throws SQLException {
        this.service = service;
        this.priceService = priceService;
        this.infoService = infoService;
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
            case 1 -> PresentationText.text(spec.getType().toString());
            case 2 -> spec.getName();
            case 3 -> spec.getDescription();
            case 4 -> PresentationText.text("Scrape price");
            case 5 -> PresentationText.text("Update");
            case 6 -> PresentationText.text("Delete");
            default -> throw new IllegalStateException("Unexpected value");
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> PresentationText.text("Id");
            case 1 -> PresentationText.text("Type");
            case 2 -> PresentationText.text("Name");
            case 3 -> PresentationText.text("Description");
            case 4 -> PresentationText.text("Scrape price");
            case 5 -> PresentationText.text("Update");
            case 6 -> PresentationText.text("Delete");
            default -> "";
        };
    }

    @Override
    public void removeRow(int row) throws SQLException {
        service.delete(data.get(row).getId());
        refresh();
    }

    @Override
    public void addRow(ItemSpec elem, Long associatedId) throws SQLException {
        service.add(elem);
        refresh();
    }

    @Override
    public void updateRow(ItemSpec elem, Long associatedId) throws SQLException {
        service.update(elem);
        refresh();
    }

    @Override
    public ItemSpec getRow(int row) {
        return data.get(row);
    }

    public double price(int row) throws InterruptedException {
        ItemSpec spec = data.get(row);
        priceService.setScraper(spec.getType());
        return priceService.computePrice(spec.getName());
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 4 || columnIndex == 5 || columnIndex == 6;
    }

    public void refresh() throws SQLException {
        data = service.getAll();
        fireTableDataChanged();
    }

    public String generateDescription(ItemSpec spec) throws IOException, InterruptedException {
        infoService.setGenerator(spec.getType());
        return infoService.generateDescriptionByAI(spec.getName());
    }

}
