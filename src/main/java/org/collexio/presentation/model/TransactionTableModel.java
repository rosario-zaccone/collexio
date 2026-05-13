package org.collexio.presentation.model;
import org.collexio.business.domain.Transaction;
import org.collexio.business.service.TransactionService;
import org.collexio.presentation.view.PresentationText;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;


public class TransactionTableModel extends AbstractTableModel implements CrudTableModel<Transaction> {
    private final static int COLUMNS = 7;
    private List<Transaction> data;
    private final TransactionService service;

    public TransactionTableModel(TransactionService service) throws SQLException {
        this.service = service;
        data = service.getAll();
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
        Transaction transaction = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> transaction.getId();
            case 1 -> {
                try {
                    yield service.getItemId(transaction.getId());
                } catch (SQLException e) {
                    yield PresentationText.text("Not available");
                }
            }
            case 2 -> String.format(Locale.ROOT, "%.2f €", transaction.getAmount());
            case 3 -> transaction.isIncome() ? PresentationText.text("Income") : PresentationText.text("Expense");
            case 4 -> transaction.getDate();
            case 5 -> PresentationText.text("Update");
            case 6 -> PresentationText.text("Delete");
            default -> throw new IllegalStateException("Unexpected value");
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> PresentationText.text("Id");
            case 1 -> PresentationText.text("Item Id");
            case 2 -> PresentationText.text("Amount");
            case 3 -> PresentationText.text("Type");
            case 4 -> PresentationText.text("Date");
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
    public void addRow(Transaction elem, Long itemId) throws SQLException, IOException {
        service.add(elem, itemId); // associatedId = itemId
        refresh();
    }

    @Override
    public void updateRow(Transaction elem, Long itemId) throws SQLException, IOException { // TODO
        service.update(elem);
        refresh();
    }


    @Override
    public Transaction getRow(int row) {
        return data.get(row);
    }

    public Long getItemId(Long transactionId) throws SQLException {
        return service.getItemId(transactionId);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 5 || columnIndex == 6;
    }

    public void refresh() throws SQLException {
        data = service.getAll();
        fireTableDataChanged();
    }

}
