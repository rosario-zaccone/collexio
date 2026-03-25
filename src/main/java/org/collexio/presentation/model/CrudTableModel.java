package org.collexio.presentation.model;

import org.collexio.business.domain.ItemSpec;

import java.sql.SQLException;

public interface CrudTableModel {
    void removeRow(int row) throws SQLException;
    void addRow(ItemSpec spec) throws SQLException;
    void updateRow(ItemSpec spec) throws SQLException;
}
